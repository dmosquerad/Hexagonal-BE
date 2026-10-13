package com.architecture.hexagonal.application.usecase.outbox.retry.impl;

import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.service.technical.outbox.OutboxService;
import com.architecture.hexagonal.application.service.technical.outbox.impl.OutboxServiceImpl;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.outbox.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.application.usecase.generated.retry.RetryOutboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetryOutboxEventsApplicationUseCaseTestIT {

  @Mock private OutboxRepositoryReadPort outboxRepositoryReadPort;
  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;
  @Mock private OutboxProcess outboxProcess;
  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;
  @Mock private OutboxService outboxService;

  @Spy private SpyTransactionReadBoundary transactionReadBoundary =
      new SpyTransactionReadBoundary();
  @Spy private SpyTransactionWriteBoundary transactionWriteBoundary =
      new SpyTransactionWriteBoundary();
  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @InjectMocks private OutboxServiceImpl outboxServiceImpl;
  @InjectMocks private RetryOutboxUseCaseImpl retryOutboxEventsUseCase;

  @Test
  void handleShouldProcessPendingEventsWhenAggregateIdsAreUnique() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox processingOutbox =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Outbox publishedOutbox =
        processingOutbox.toBuilder().status(OutboxStatusVo.PUBLISHED).build();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler())
        .thenReturn(
            SchedulerOutboxConfigurationVoTestDataBuilder.builder()
                .build()
                .schedulerOutboxConfigurationVo());
    Mockito.when(
            outboxRepositoryWritePort.upsertByEventIdentity(
                ArgumentMatchers.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.when(
            outboxService.find(
                ArgumentMatchers.any(OutboxStatusAndAggregateProjector.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (OutboxStatusAndAggregateProjector projector) ->
                    outboxServiceImpl.find(projector)));
    Mockito.when(outboxService.process(ArgumentMatchers.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.answer((Outbox event) -> outboxServiceImpl.process(event)));
    Mockito.when(
            outboxRepositoryWritePort.claimPendingForProcessing(
                ArgumentMatchers.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.answer((Outbox event) -> Optional.of(event)));
    Mockito.when(
            outboxRepositoryReadPort.findByStatusAndAggregateType(ArgumentMatchers.any()))
        .thenAnswer(
            AdditionalAnswers.answer(
                (OutboxStatusAndAggregateProjector projector) ->
                    projector.aggregateType() == outbox.aggregateType()
                        ? List.of(outbox)
                        : List.of()));

    retryOutboxEventsUseCase.handle(new RetryOutboxeventCommand());

    Mockito.verify(
            transactionReadBoundary,
            Mockito.times(AggregateTypeVo.values().length))
        .execute(ArgumentMatchers.any());
    Mockito.verify(transactionWriteBoundary)
        .executeWithoutResult(ArgumentMatchers.any(Runnable.class));
    Mockito.verify(outboxService)
        .find(ArgumentMatchers.any(OutboxStatusAndAggregateProjector.class));
    Mockito.verify(outboxService).process(ArgumentMatchers.any(Outbox.class));
    Mockito.verify(
            outboxRepositoryReadPort,
            Mockito.times(AggregateTypeVo.values().length))
        .findByStatusAndAggregateType(
            ArgumentMatchers.any(OutboxStatusAndAggregateProjector.class));
    Mockito.verify(outboxProcess).process(processingOutbox);
    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processingOutbox);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(publishedOutbox);
    Mockito.verify(outboxService).process(outbox);
  }

  @Test
  void handleShouldBlockDuplicatePendingEventWhenAggregateIdIsRepeated() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox duplicateOutbox =
        OutboxTestDataBuilder.builder().build().outbox();
    final Outbox processingOutbox =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Set<UUID> claimedEventIds = new HashSet<>();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler())
        .thenReturn(
            SchedulerOutboxConfigurationVoTestDataBuilder.builder()
                .build()
                .schedulerOutboxConfigurationVo());
    Mockito.when(
            outboxRepositoryWritePort.upsertByEventIdentity(
                ArgumentMatchers.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.when(
            outboxService.find(
                ArgumentMatchers.any(OutboxStatusAndAggregateProjector.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (OutboxStatusAndAggregateProjector projector) ->
                    outboxServiceImpl.find(projector)));
    Mockito.when(outboxService.process(ArgumentMatchers.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.answer((Outbox event) -> outboxServiceImpl.process(event)));
    Mockito.when(
            outboxRepositoryReadPort.findByStatusAndAggregateType(ArgumentMatchers.any()))
        .thenAnswer(
            AdditionalAnswers.answer(
                (OutboxStatusAndAggregateProjector projector) ->
                    projector.aggregateType() == outbox.aggregateType()
                        ? List.of(outbox, duplicateOutbox)
                        : List.of()));
    Mockito.when(
            outboxRepositoryWritePort.claimPendingForProcessing(
                ArgumentMatchers.any(Outbox.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (Outbox candidate) ->
                    claimedEventIds.add(candidate.outboxId())
                        ? Optional.of(candidate)
                        : Optional.empty()));

    retryOutboxEventsUseCase.handle(new RetryOutboxeventCommand());

    Mockito.verify(
            transactionReadBoundary,
            Mockito.times(AggregateTypeVo.values().length))
        .execute(ArgumentMatchers.any());
    Mockito.verify(transactionWriteBoundary, Mockito.times(2))
        .executeWithoutResult(ArgumentMatchers.any(Runnable.class));
    Mockito.verify(outboxService)
        .find(ArgumentMatchers.any(OutboxStatusAndAggregateProjector.class));
    Mockito.verify(outboxService, Mockito.times(2)).process(outbox);
    Mockito.verify(outboxProcess).process(processingOutbox);
    Mockito.verify(outboxRepositoryWritePort, Mockito.times(2))
        .claimPendingForProcessing(processingOutbox);
  }
}
