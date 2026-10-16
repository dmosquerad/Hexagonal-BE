package com.architecture.hexagonal.application.service.technical.inbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.architecture.hexagonal.application.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.service.technical.inbox.impl.InboxServiceImpl;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InboxServiceImplTest {

  @Mock private InboxRepositoryReadPort inboxRepositoryReadPort;
  @Mock private InboxRepositoryWritePort inboxRepositoryWritePort;
  @Mock private InboxProcess inboxProcess;
  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;
  private InboxServiceImpl inboxService;

  private Inbox pendingInbox;

  @BeforeEach
  void setUp() {
    inboxService =
        new InboxServiceImpl(
            inboxRepositoryReadPort,
            inboxRepositoryWritePort,
            inboxProcess,
            schedulerConfigurationPort,
            Clock.fixed(Instant.parse("2026-10-13T10:00:00Z"), ZoneOffset.UTC));
    pendingInbox =
        Inbox.builder()
            .payload(
                PayloadVo.builder()
                    .messageHeader(
                        MessageHeaderVo.builder()
                            .messageId(UUID.randomUUID())
                            .messageDate(OffsetDateTime.parse("2026-10-13T09:00:00Z"))
                            .build())
                    .data(new Object())
                    .build())
            .aggregateType(AggregateTypeVo.USER)
            .action("USER_CREATED")
            .status(StatusVo.PENDING)
            .retryCount(0)
            .createdAt(OffsetDateTime.parse("2026-10-13T09:00:00Z"))
            .build();
  }

  @Test
  void findPendingShouldFilterByAggregateType() {
    when(inboxRepositoryReadPort.findByStatusAndAggregateType(
            StatusVo.PENDING, AggregateTypeVo.USER))
        .thenReturn(List.of(pendingInbox));

    final List<Inbox> result = inboxService.findPending(AggregateTypeVo.USER);

    assertThat(result).containsExactly(pendingInbox);
    verify(inboxRepositoryReadPort)
        .findByStatusAndAggregateType(StatusVo.PENDING, AggregateTypeVo.USER);
  }

  @Test
  void processShouldMarkMessageProcessedAfterSuccessfulDispatch() {
    final Inbox claimed = claimedInbox(1);
    final Inbox processed = claimed.toBuilder().status(StatusVo.PROCESSED).build();
    when(inboxRepositoryWritePort.claimPendingForProcessing(any()))
        .thenReturn(Optional.of(claimed));
    when(schedulerConfigurationPort.getInboxScheduler()).thenReturn(configuration(5));
    when(inboxRepositoryWritePort.upsertByMessageIdentity(any())).thenReturn(processed);

    final Inbox result = inboxService.process(pendingInbox);

    assertThat(result.status()).isEqualTo(StatusVo.PROCESSED);
    verify(inboxProcess).process(claimed);
    verify(inboxRepositoryWritePort).upsertByMessageIdentity(processed);
  }

  @Test
  void processShouldReturnMessageToPendingWhenRetryIsAvailable() {
    final Inbox claimed = claimedInbox(1);
    final Inbox pending = claimed.toBuilder().status(StatusVo.PENDING).build();
    when(inboxRepositoryWritePort.claimPendingForProcessing(any()))
        .thenReturn(Optional.of(claimed));
    when(schedulerConfigurationPort.getInboxScheduler()).thenReturn(configuration(2));
    org.mockito.Mockito.doThrow(new IllegalStateException("downstream unavailable"))
        .when(inboxProcess)
        .process(claimed);
    when(inboxRepositoryWritePort.upsertByMessageIdentity(any())).thenReturn(pending);

    final Inbox result = inboxService.process(pendingInbox);

    assertThat(result.status()).isEqualTo(StatusVo.PENDING);
    verify(inboxRepositoryWritePort).upsertByMessageIdentity(pending);
  }

  @Test
  void processShouldMarkMessageFailedAfterLastAttempt() {
    final Inbox claimed = claimedInbox(2);
    final Inbox failed = claimed.toBuilder().status(StatusVo.FAILED).build();
    when(inboxRepositoryWritePort.claimPendingForProcessing(any()))
        .thenReturn(Optional.of(claimed));
    when(schedulerConfigurationPort.getInboxScheduler()).thenReturn(configuration(2));
    when(inboxRepositoryWritePort.upsertByMessageIdentity(any())).thenReturn(failed);

    final Inbox result = inboxService.process(pendingInbox.toBuilder().retryCount(1).build());

    assertThat(result.status()).isEqualTo(StatusVo.FAILED);
    verify(inboxRepositoryWritePort).upsertByMessageIdentity(failed);
  }

  @Test
  void processShouldNotDispatchWhenAnotherWorkerAlreadyClaimedMessage() {
    when(inboxRepositoryWritePort.claimPendingForProcessing(any())).thenReturn(Optional.empty());

    assertThat(inboxService.process(pendingInbox)).isEqualTo(pendingInbox);

    verify(inboxProcess, never()).process(any());
  }

  private Inbox claimedInbox(final int retryCount) {
    return pendingInbox.toBuilder()
        .status(StatusVo.PROCESSING)
        .retryCount(retryCount)
        .processedAt(OffsetDateTime.parse("2026-10-13T10:00:00Z"))
        .build();
  }

  private SchedulerInboxConfigurationVo configuration(final int maxRetries) {
    return SchedulerInboxConfigurationVo.builder()
        .pollingInterval(Duration.ofMinutes(1))
        .maxRetries(maxRetries)
        .build();
  }
}
