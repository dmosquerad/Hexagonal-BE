package com.architecture.hexagonal.application.service.inbox;

import com.architecture.hexagonal.application.service.inbox.impl.InboxServiceImpl;
import com.architecture.hexagonal.domain.databuilder.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.inbox.SchedulerInboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.domain.port.out.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.domain.port.out.repository.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.repository.inbox.InboxRepositoryWritePort;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InboxServiceImplTest {
  @Mock private InboxRepositoryReadPort inboxRepositoryReadPort;
  @Mock private InboxRepositoryWritePort inboxRepositoryWritePort;
  @Mock private InboxProcess inboxProcess;
  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;

  @InjectMocks private InboxServiceImpl inboxService;

  private MockedStatic<Instant> instantMock;

  @BeforeEach
  void setUp() {
    instantMock = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS);
  }

  @AfterEach
  void tearDown() {
    instantMock.close();
  }

  @Test
  void saveShouldReturnSavedInbox() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    Mockito.when(inboxRepositoryWritePort.save(inbox)).thenReturn(inbox);

    final Inbox result = inboxService.save(inbox);

    AssertionsForClassTypes.assertThat(result).isSameAs(inbox);
    Mockito.verify(inboxRepositoryWritePort).save(inbox);
  }

  @Test
  void findPendingShouldFilterByAggregateType() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    Mockito.when(
            inboxRepositoryReadPort.findByStatusAndAggregateType(
                StatusVo.PENDING, AggregateTypeVo.USER))
        .thenReturn(List.of(inbox));

    final List<Inbox> result = inboxService.findPending(AggregateTypeVo.USER);

    AssertionsForInterfaceTypes.assertThat(result).containsExactly(inbox);
    Mockito.verify(inboxRepositoryReadPort)
        .findByStatusAndAggregateType(StatusVo.PENDING, AggregateTypeVo.USER);
  }

  @Test
  void processShouldMarkMessageProcessedAfterSuccessfulDispatch() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    final Inbox processing =
        inbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Inbox processed = processing.toBuilder().status(StatusVo.PROCESSED).build();
    Mockito.when(inboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Inbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(schedulerConfigurationPort.getInboxScheduler())
        .thenReturn(
            SchedulerInboxConfigurationVoTestDataBuilder.builder()
                .build()
                .schedulerInboxConfigurationVo());
    Mockito.when(inboxRepositoryWritePort.upsertByMessageIdentity(Mockito.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    AssertionsForClassTypes.assertThat(inboxService.process(inbox))
        .usingRecursiveComparison()
        .isEqualTo(processed);

    Mockito.verify(inboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(schedulerConfigurationPort).getInboxScheduler();
    Mockito.verify(inboxProcess).process(processing);
    Mockito.verify(inboxRepositoryWritePort).upsertByMessageIdentity(processed);
  }

  @Test
  void processShouldReturnMessageToPendingWhenRetryIsAvailable() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    final Inbox processing =
        inbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Inbox pending = processing.toBuilder().status(StatusVo.PENDING).build();
    Mockito.when(inboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Inbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(schedulerConfigurationPort.getInboxScheduler())
        .thenReturn(
            SchedulerInboxConfigurationVoTestDataBuilder.builder()
                .build()
                .schedulerInboxConfigurationVo());
    Mockito.when(inboxRepositoryWritePort.upsertByMessageIdentity(Mockito.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.doThrow(new RuntimeException("consumer unavailable"))
        .when(inboxProcess)
        .process(Mockito.any(Inbox.class));

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    AssertionsForClassTypes.assertThat(inboxService.process(inbox))
        .usingRecursiveComparison()
        .isEqualTo(pending);

    Mockito.verify(inboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(inboxProcess).process(processing);
    Mockito.verify(inboxRepositoryWritePort).upsertByMessageIdentity(pending);
  }

  @Test
  void processShouldMarkMessageFailedAfterLastAttempt() {
    final Inbox inbox = InboxTestDataBuilder.builder().retryCount(4).build().inbox();
    final Inbox processing =
        inbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(5)
            .processedAt(DateVoFactory.from(TestTime.FIXED_INSTANT))
            .build();
    final Inbox failed = processing.toBuilder().status(StatusVo.FAILED).build();
    final SchedulerInboxConfigurationVo schedulerConfiguration =
        SchedulerInboxConfigurationVoTestDataBuilder.builder()
            .maxRetries(5)
            .build()
            .schedulerInboxConfigurationVo();
    Mockito.when(inboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Inbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(schedulerConfigurationPort.getInboxScheduler()).thenReturn(schedulerConfiguration);
    Mockito.when(inboxRepositoryWritePort.upsertByMessageIdentity(Mockito.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    AssertionsForClassTypes.assertThat(inboxService.process(inbox))
        .usingRecursiveComparison()
        .isEqualTo(failed);

    Mockito.verify(inboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(inboxRepositoryWritePort).upsertByMessageIdentity(failed);
    Mockito.verify(inboxProcess, Mockito.never()).process(Mockito.any(Inbox.class));
  }

  @Test
  void processShouldNotDispatchWhenAnotherWorkerAlreadyClaimedMessage() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    Mockito.when(inboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Inbox.class)))
        .thenReturn(Optional.empty());

    instantMock.when(Instant::now).thenReturn(TestTime.FIXED_INSTANT);
    AssertionsForClassTypes.assertThat(inboxService.process(inbox)).isSameAs(inbox);

    Mockito.verify(schedulerConfigurationPort, Mockito.never()).getInboxScheduler();
    Mockito.verify(inboxProcess, Mockito.never()).process(Mockito.any(Inbox.class));
    Mockito.verify(inboxRepositoryWritePort, Mockito.never())
        .upsertByMessageIdentity(Mockito.any(Inbox.class));
  }
}
