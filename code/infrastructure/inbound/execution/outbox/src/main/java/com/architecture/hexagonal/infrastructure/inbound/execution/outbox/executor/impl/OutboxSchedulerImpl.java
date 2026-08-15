package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.impl;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.execution.outbox.executor.OutboxScheduler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
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
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addFixedDelayTask(
        this::scheduleRetry, schedulerConfigurationPort.getSchedulerOutbox().pollingInterval());
  }

  public void scheduleRetry() {
    commandBus.execute(new RetryOutboxeventCommandDto());
  }
}
