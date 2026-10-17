package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl;

import com.architecture.hexagonal.application.port.out.scheduler.SchedulerConfigurationPort;
import com.architecture.hexagonal.contract.application.usecase.generated.retry.RetryOutboxeventCommand;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.OutboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxSchedulerImpl implements OutboxScheduler, SchedulingConfigurer {

  private final SchedulerConfigurationPort schedulerConfigurationPort;
  private final CommandBus commandBus;

  @Override
  public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addFixedDelayTask(
        this::scheduleRetry, schedulerConfigurationPort.getOutboxScheduler().pollingInterval());
  }

  public void scheduleRetry() {
    commandBus.execute(new RetryOutboxeventCommand());
  }
}
