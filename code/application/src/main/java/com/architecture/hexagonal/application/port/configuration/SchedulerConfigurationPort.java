package com.architecture.hexagonal.application.port.configuration;

import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;

public interface SchedulerConfigurationPort {

  SchedulerOutboxConfigurationVo getSchedulerOutbox();
}
