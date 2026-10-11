package com.architecture.hexagonal.application.port.out.scheduler;

import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;

public interface SchedulerConfigurationPort {

  SchedulerOutboxConfigurationVo getOutboxScheduler();

  SchedulerInboxConfigurationVo getInboxScheduler();
}
