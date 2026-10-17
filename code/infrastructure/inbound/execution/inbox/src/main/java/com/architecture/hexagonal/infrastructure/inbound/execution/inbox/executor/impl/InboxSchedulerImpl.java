package com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.impl;

import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.contract.bus.model.retry.command.RetryInboxeventCommand;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.execution.inbox.executor.InboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboxSchedulerImpl implements InboxScheduler, SchedulingConfigurer {

  private final SchedulerConfigurationPort schedulerConfigurationPort;
  private final CommandBus commandBus;

  @Override
  public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addFixedDelayTask(
        this::scheduleProcess, schedulerConfigurationPort.getInboxScheduler().pollingInterval());
  }

  @Override
  public void scheduleProcess() {
    commandBus.execute(new RetryInboxeventCommand());
  }
}
