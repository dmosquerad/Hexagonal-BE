package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandBus;
import com.architecture.hexagonal.contract.application.generated.retry.RetryOutboxeventCommand;
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
  void scheduleRetryShouldSendRetryOutboxCommand() {
    outboxRetrySchedulerImpl.scheduleRetry();

    Mockito.verify(commandBus).execute(Mockito.any(RetryOutboxeventCommand.class));
  }
}
