package com.architecture.hexagonal.application.technical.outbox.process.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.application.testutils.model.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.application.testutils.model.vo.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.application.testutils.usecase.technical.outbox.process.ProcessOutboxInputTestDataBuilder;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.impl.ProcessOutboxUseCaseImpl;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import java.time.Clock;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessOutboxUseCaseImplTest {

  @InjectMocks private ProcessOutboxUseCaseImpl processOutboxEventUseCaseImpl;

  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;
  @Mock private OutboxProcessPort outboxProcessPort;

  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;
  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void executeShouldPublishEventWhenRetrySucceeds() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processOutboxInput.outbox()));

    processOutboxEventUseCaseImpl.execute(processOutboxInput);

    Mockito.verify(schedulerConfigurationPort).getOutboxScheduler();
    Mockito.verify(outboxProcessPort).process(Mockito.any(Outbox.class));
    Mockito.verify(clock).instant();
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void executeShouldNotPublishEventWhenClaimWasLost() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.empty());

    final Outbox result = processOutboxEventUseCaseImpl.execute(processOutboxInput);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(processOutboxInput.outbox());

    Mockito.verify(outboxProcessPort, Mockito.never()).process(Mockito.any(Outbox.class));
    Mockito.verify(outboxRepositoryWritePort, Mockito.never())
        .upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void executeShouldKeepEventPendingWhenPublishingFails() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processOutboxInput.outbox()));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenReturn(processOutboxInput.outbox());
    Mockito.doThrow(new RuntimeException("broker unavailable"))
        .when(outboxProcessPort)
        .process(Mockito.any(Outbox.class));

    final Outbox result = processOutboxEventUseCaseImpl.execute(processOutboxInput);

    AssertionsForClassTypes.assertThat(result.status()).isEqualTo(OutboxStatusVo.PENDING);

    Mockito.verify(outboxProcessPort).process(Mockito.any(Outbox.class));
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void executeShouldPublishEventOnNextRetryAfterInitialFailure() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();
    final Outbox pendingRetry =
        processOutboxInput.outbox().toBuilder()
            .retryCount(1)
            .status(OutboxStatusVo.PENDING)
            .build();
    final Outbox published =
        pendingRetry.toBuilder().retryCount(2).status(OutboxStatusVo.PUBLISHED).build();

    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processOutboxInput.outbox()), Optional.of(pendingRetry));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenReturn(pendingRetry, published);
    Mockito.doThrow(new RuntimeException("broker unavailable"))
        .doNothing()
        .when(outboxProcessPort)
        .process(Mockito.any(Outbox.class));

    final Outbox firstAttempt = processOutboxEventUseCaseImpl.execute(processOutboxInput);
    final Outbox secondAttempt =
        processOutboxEventUseCaseImpl.execute(
            ProcessOutboxInput.builder().outbox(firstAttempt).build());

    AssertionsForClassTypes.assertThat(firstAttempt)
        .usingRecursiveComparison()
        .isEqualTo(pendingRetry);
    AssertionsForClassTypes.assertThat(secondAttempt)
        .usingRecursiveComparison()
        .isEqualTo(published);

    Mockito.verify(outboxProcessPort, Mockito.times(2)).process(Mockito.any(Outbox.class));
    Mockito.verify(outboxRepositoryWritePort, Mockito.times(2))
        .upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void executeShouldMarkEventFailedWhenMaxRetriesReached() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .maxRetries(5)
            .build()
            .schedulerOutboxConfigurationVo();
    final Outbox expectedFailed =
        OutboxTestDataBuilder.builder()
            .retryCount(5)
            .status(OutboxStatusVo.FAILED)
            .build()
            .outboxDo();
    Mockito.when(schedulerConfigurationPort.getOutboxScheduler()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.claimPendingForProcessing(Mockito.any(Outbox.class)))
        .thenReturn(Optional.of(processOutboxInput.outbox()));
    Mockito.when(outboxRepositoryWritePort.upsertByEventIdentity(Mockito.any(Outbox.class)))
        .thenReturn(expectedFailed);

    final Outbox result = processOutboxEventUseCaseImpl.execute(processOutboxInput);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedFailed);

    Mockito.verify(clock).instant();
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }
}
