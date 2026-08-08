package com.architecture.hexagonal.infrastructure.inbound.execution.outbox.data.vo;

import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import lombok.Builder;

import java.time.Duration;

@Builder
public class SchedulerOutboxConfigurationVoTestDataBuilder {

  @Builder.Default
  private Duration pollingInterval = Duration.parse("PT1M");

  @Builder.Default
  private int maxRetries = 5;

  public SchedulerOutboxConfigurationVo schedulerOutboxConfigurationVo() {
    return SchedulerOutboxConfigurationVo.builder()
        .pollingInterval(pollingInterval)
        .maxRetries(maxRetries)
        .build();
  }
}
