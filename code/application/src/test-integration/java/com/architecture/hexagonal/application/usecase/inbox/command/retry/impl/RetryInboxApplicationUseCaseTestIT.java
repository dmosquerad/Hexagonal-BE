package com.architecture.hexagonal.application.usecase.inbox.command.retry.impl;

import com.architecture.hexagonal.application.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.application.service.inbox.impl.InboxServiceImpl;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.bus.model.retry.command.RetryInboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.inbox.SchedulerInboxConfigurationVoTestDataBuilder;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetryInboxApplicationUseCaseTestIT {

  @Mock private InboxRepositoryReadPort inboxRepositoryReadPort;
  @Mock private InboxRepositoryWritePort inboxRepositoryWritePort;
  @Mock private InboxProcess inboxProcess;
  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;
  @Mock private InboxService inboxService;

  @Spy private Clock clock = TestClock.FIXED_CLOCK;
  @Spy private SpyTransactionReadBoundary transactionReadBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private InboxServiceImpl inboxServiceImpl;
  @InjectMocks private RetryInboxUseCaseImpl retryInboxUseCase;

  @Test
  void handleShouldProcessPendingEventsForUserAggregateType() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    final Inbox processingInbox =
        inbox.toBuilder()
            .status(StatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Inbox processedInbox = processingInbox.toBuilder().status(StatusVo.PROCESSED).build();

    Mockito.when(
            inboxRepositoryReadPort.findByStatusAndAggregateType(
                ArgumentMatchers.eq(StatusVo.PENDING),
                ArgumentMatchers.any(AggregateTypeVo.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (StatusVo status, AggregateTypeVo aggregateType) ->
                    aggregateType == inbox.aggregateType() ? List.of(inbox) : List.of()));
    Mockito.when(
            inboxRepositoryWritePort.claimPendingForProcessing(
                ArgumentMatchers.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.answer((Inbox candidate) -> Optional.of(candidate)));
    Mockito.when(schedulerConfigurationPort.getInboxScheduler())
        .thenReturn(
            SchedulerInboxConfigurationVoTestDataBuilder.builder()
                .build()
                .schedulerInboxConfigurationVo());
    Mockito.when(
            inboxRepositoryWritePort.upsertByMessageIdentity(ArgumentMatchers.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.when(inboxService.findPending(ArgumentMatchers.any(AggregateTypeVo.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (AggregateTypeVo aggregateType) -> inboxServiceImpl.findPending(aggregateType)));
    Mockito.when(inboxService.process(ArgumentMatchers.any(Inbox.class)))
        .thenAnswer(AdditionalAnswers.answer((Inbox event) -> inboxServiceImpl.process(event)));

    final Void result = retryInboxUseCase.handle(new RetryInboxeventCommand());

    AssertionsForClassTypes.assertThat(result).isNull();
    Mockito.verify(transactionReadBoundary, Mockito.times(AggregateTypeVo.values().length))
        .execute(ArgumentMatchers.any());
    Mockito.verify(inboxService).findPending(ArgumentMatchers.any(AggregateTypeVo.class));
    Mockito.verify(inboxService).process(inbox);
    Mockito.verify(inboxRepositoryReadPort, Mockito.times(AggregateTypeVo.values().length))
        .findByStatusAndAggregateType(
            ArgumentMatchers.eq(StatusVo.PENDING),
            ArgumentMatchers.any(AggregateTypeVo.class));
    Mockito.verify(inboxRepositoryWritePort).claimPendingForProcessing(processingInbox);
    Mockito.verify(schedulerConfigurationPort).getInboxScheduler();
    Mockito.verify(inboxProcess).process(processingInbox);
    Mockito.verify(inboxRepositoryWritePort).upsertByMessageIdentity(processedInbox);
  }
}
