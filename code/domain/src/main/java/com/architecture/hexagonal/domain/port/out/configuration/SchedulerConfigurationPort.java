package com.architecture.hexagonal.domain.port.out.configuration;

import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;

public interface SchedulerConfigurationPort {

  SchedulerOutboxConfigurationVo getOutboxScheduler();

  SchedulerInboxConfigurationVo getInboxScheduler();
}
