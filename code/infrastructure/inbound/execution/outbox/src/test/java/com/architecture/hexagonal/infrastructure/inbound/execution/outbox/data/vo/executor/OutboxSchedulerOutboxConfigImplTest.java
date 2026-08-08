package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.data.vo.executor;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl.OutboxSchedulerImpl;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxSchedulerOutboxConfigImplTest {

  @Mock private CommandBus commandBus;

  @InjectMocks private OutboxSchedulerImpl outboxRetrySchedulerImpl;

  @Test
  void scheduleRetry_shouldSendRetryOutboxCommand() {
    outboxRetrySchedulerImpl.scheduleRetry();

    Mockito.verify(commandBus).execute(Mockito.any(RetryOutboxeventCommandDto.class));
  }
}
