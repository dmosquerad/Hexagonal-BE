package com.architecture.hexagonal.application.technical.outbox.process.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.SchedulerOutboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import java.time.Clock;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessOutboxEventUseCaseImplTest {

  @InjectMocks private ProcessOutboxEventUseCaseImpl processOutboxEventUseCaseImpl;

  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Mock private OutboxProcessPort outboxProcessPort;

  @Mock private SchedulerConfigurationPort schedulerConfigurationPort;

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void executeShouldPublishEventWhenRetrySucceeds() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    final SchedulerOutboxConfigurationVo schedulerConfig =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();
    Mockito.when(schedulerConfigurationPort.getSchedulerOutbox()).thenReturn(schedulerConfig);

    processOutboxEventUseCaseImpl.execute(outbox);

    Mockito.verify(schedulerConfigurationPort).getSchedulerOutbox();
    Mockito.verify(outboxProcessPort).process(ArgumentMatchers.any(Outbox.class));
    Mockito.verify(clock).instant();
    Mockito.verify(outboxRepositoryWritePort, Mockito.times(2))
        .save(ArgumentMatchers.any(Outbox.class));
  }

  @Test
  void executeShouldMarkEventFailedWhenMaxRetriesReached() {
    final Outbox outbox = OutboxTestDataBuilder.builder().retryCount(5).build().outboxDo();
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
    Mockito.when(schedulerConfigurationPort.getSchedulerOutbox()).thenReturn(schedulerConfig);
    Mockito.when(outboxRepositoryWritePort.save(ArgumentMatchers.any(Outbox.class)))
        .thenReturn(expectedFailed);

    final Outbox result = processOutboxEventUseCaseImpl.execute(outbox);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedFailed);
    Mockito.verifyNoInteractions(outboxProcessPort);
    Mockito.verify(clock).instant();
    Mockito.verify(outboxRepositoryWritePort).save(ArgumentMatchers.any(Outbox.class));
  }
}
