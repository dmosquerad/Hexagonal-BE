package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerInboxConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerOutboxConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfigurationAdapterImpl implements SchedulerConfigurationPort {

  private final SchedulerOutboxConfig schedulerOutboxConfig;

  private final SchedulerInboxConfig schedulerInboxConfig;

  @Override
  public SchedulerOutboxConfigurationVo getOutboxScheduler() {
    return SchedulerOutboxConfigurationVo.builder()
        .pollingInterval(schedulerOutboxConfig.getPollingInterval())
        .maxRetries(schedulerOutboxConfig.getMaxRetries())
        .build();
  }

  @Override
  public SchedulerInboxConfigurationVo getInboxScheduler() {
    return SchedulerInboxConfigurationVo.builder()
        .pollingInterval(schedulerInboxConfig.getPollingInterval())
        .maxRetries(schedulerInboxConfig.getMaxRetries())
        .build();
  }
}
