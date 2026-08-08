package com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.InboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboxSchedulerImpl implements InboxScheduler, SchedulingConfigurer {

  private final SchedulerConfigurationPort schedulerConfigurationPort;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addFixedDelayTask(
        this::scheduleProcess,
        schedulerConfigurationPort.getSchedulerOutbox().getPollingInterval());
  }

  @Override
  public void scheduleProcess() {
    // TODO: implement inbox processing logic
  }
}
