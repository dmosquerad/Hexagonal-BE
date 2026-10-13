package com.architecture.hexagonal.application.service.technical.outbox;

import com.architecture.hexagonal.application.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.projector.technical.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.service.technical.outbox.impl.OutboxServiceImpl;
import com.architecture.hexagonal.application.testutils.model.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.application.testutils.model.vo.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.time.TestClock;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxServiceImplTest {

  @Mock private OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Mock private OutboxProcess outboxProcess;

  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @InjectMocks private OutboxServiceImpl outboxService;

  @Test
  void findShouldReturnPendingEvents() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    final OutboxStatusAndAggregateProjector projector =
        OutboxStatusAndAggregateProjector.builder()
            .aggregateType(AggregateTypeVo.USER)
            .status(OutboxStatusVo.PENDING)
            .build();

    Mockito.when(outboxRepositoryReadPort.findByStatusAndAggregateType(projector))
        .thenReturn(List.of(outbox));

    final List<Outbox> result = outboxService.find(projector);

    AssertionsForInterfaceTypes.assertThat(result).containsExactly(outbox);
    Mockito.verify(outboxRepositoryReadPort).findByStatusAndAggregateType(projector);
  }

  @Test
  void processShouldPublishEventWhenRetrySucceeds() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    final Outbox processing =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Outbox published = processing.toBuilder().status(OutboxStatusVo.PUBLISHED).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(published);
    Mockito.verify(schedulerConfigurationPort).getOutboxScheduler();
    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(outboxProcess).process(processing);
    Mockito.verify(clock).instant();
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(published);
  }

  @Test
  void processShouldNotPublishEventWhenClaimWasLost() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.empty());

    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(outbox);

    Mockito.verify(outboxProcess, Mockito.never()).process(Mockito.any(Outbox.class));
    Mockito.verify(outboxRepositoryWritePort, Mockito.never())
        .upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void processShouldKeepEventPendingWhenPublishingFails() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    final Outbox processing =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Outbox pending = processing.toBuilder().status(OutboxStatusVo.PENDING).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.doThrow(new RuntimeException("broker unavailable"))
        .when(outboxProcess)
        .process(Mockito.any(Outbox.class));

    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(pending);

    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(outboxProcess).process(processing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(pending);
  }

  @Test
  void processShouldPublishEventOnNextRetryAfterInitialFailure() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    final Outbox firstProcessing =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(1)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Outbox pendingRetry = firstProcessing.toBuilder().status(OutboxStatusVo.PENDING).build();
    final Outbox secondProcessing =
        pendingRetry.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(2)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Outbox published = secondProcessing.toBuilder().status(OutboxStatusVo.PUBLISHED).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(firstProcessing), Optional.of(secondProcessing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());
    Mockito.doThrow(new RuntimeException("broker unavailable"))
        .doNothing()
        .when(outboxProcess)
        .process(Mockito.any(Outbox.class));

    final Outbox firstAttempt = outboxService.process(outbox);
    final Outbox secondAttempt = outboxService.process(firstAttempt);

    AssertionsForClassTypes.assertThat(firstAttempt)
        .usingRecursiveComparison()
        .isEqualTo(pendingRetry);
    AssertionsForClassTypes.assertThat(secondAttempt)
        .usingRecursiveComparison()
        .isEqualTo(published);

    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(firstProcessing);
    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(secondProcessing);
    Mockito.verify(outboxProcess).process(firstProcessing);
    Mockito.verify(outboxProcess).process(secondProcessing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(pendingRetry);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(published);
  }

  @Test
  void processShouldMarkEventFailedWhenMaxRetriesReached() {
    final Outbox outbox = OutboxTestDataBuilder.builder().retryCount(5).build().outboxDo();
    final Outbox processing =
        outbox.toBuilder()
            .status(OutboxStatusVo.PROCESSING)
            .retryCount(6)
            .processedAt(TestClock.FIXED_INSTANT.atOffset(ZoneOffset.UTC))
            .build();
    final Outbox expectedFailed = processing.toBuilder().status(OutboxStatusVo.FAILED).build();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .maxRetries(5)
            .build()
            .schedulerOutboxConfigurationVo();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processing));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenAnswer(AdditionalAnswers.returnsFirstArg());

    final Outbox result = outboxService.process(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedFailed);

    Mockito.verify(outboxRepositoryWritePort).claimPendingForProcessing(processing);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(expectedFailed);
    Mockito.verify(outboxProcess, Mockito.never()).process(Mockito.any(Outbox.class));
    Mockito.verify(clock, Mockito.times(2)).instant();
  }
}
