package com.architecture.hexagonal.application.technical.outbox.block.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.application.testutils.data.entity.OutboxDoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
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
class BlockOutboxEventUseCaseImplTest {

  @InjectMocks private BlockOutboxEventUseCaseImpl blockOutboxEventUseCaseImpl;

  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void executeShouldSaveBlockedEvent() {
    final OutboxDo outboxDo = OutboxDoTestDataBuilder.builder().build().outboxDo();
    final OutboxDo expectedBlocked =
        OutboxDoTestDataBuilder.builder().status(OutboxStatusVo.BLOCKED).build().outboxDo();
    Mockito.when(outboxRepositoryWritePort.save(ArgumentMatchers.any(OutboxDo.class)))
        .thenReturn(expectedBlocked);

    final OutboxDo result = blockOutboxEventUseCaseImpl.execute(outboxDo);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expectedBlocked);
    Mockito.verify(outboxRepositoryWritePort).save(ArgumentMatchers.any(OutboxDo.class));
    Mockito.verify(clock).instant();
  }
}
