package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl;

import com.architecture.hexagonal.contract.bus.model.retry.RetryOutboxeventCommand;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxSchedulerImplTest {

  @Mock private CommandBus commandBus;

  @InjectMocks private OutboxSchedulerImpl outboxRetrySchedulerImpl;

  @Test
  void scheduleRetryShouldSendRetryOutboxCommand() {
    outboxRetrySchedulerImpl.scheduleRetry();

    Mockito.verify(commandBus).execute(Mockito.any(RetryOutboxeventCommand.class));
  }
}
