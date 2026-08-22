package com.architecture.hexagonal.infrastructure.inbound.execution.inbox.data.vo.executor;

import com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.impl.InboxSchedulerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InboxSchedulerImplTest {

  @InjectMocks private InboxSchedulerImpl inboxSchedulerImpl;

  @Test
  void scheduleProcess_shouldNotThrowException() {
    inboxSchedulerImpl.scheduleProcess();
  }
}
