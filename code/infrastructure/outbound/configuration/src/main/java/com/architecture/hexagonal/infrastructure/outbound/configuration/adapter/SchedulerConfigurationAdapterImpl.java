package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.application.port.configuration.SchedulerConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerOutboxConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfigurationAdapterImpl implements SchedulerConfigurationPort {

  private final SchedulerOutboxConfig schedulerOutboxConfig;

  @Override
  public SchedulerOutboxConfigurationVo getSchedulerOutbox() {
    return SchedulerOutboxConfigurationVo.builder()
        .pollingInterval(schedulerOutboxConfig.getPollingInterval())
        .maxRetries(schedulerOutboxConfig.getMaxRetries())
        .build();
  }
}
