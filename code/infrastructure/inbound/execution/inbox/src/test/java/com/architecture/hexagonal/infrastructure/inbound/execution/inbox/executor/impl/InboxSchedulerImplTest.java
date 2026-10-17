package com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.impl;

import com.architecture.hexagonal.contract.bus.model.retry.command.RetryInboxeventCommand;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InboxSchedulerImplTest {

  @InjectMocks private InboxSchedulerImpl inboxSchedulerImpl;
  @Mock private CommandBus commandBus;

  @Test
  void scheduleProcessShouldDispatchInboxRetryCommand() {
    inboxSchedulerImpl.scheduleProcess();
    Mockito.verify(commandBus).execute(Mockito.any(RetryInboxeventCommand.class));
  }
}
