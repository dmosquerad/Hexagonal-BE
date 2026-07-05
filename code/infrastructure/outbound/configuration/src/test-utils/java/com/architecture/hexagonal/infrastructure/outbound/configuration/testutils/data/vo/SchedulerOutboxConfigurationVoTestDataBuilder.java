package com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo;

import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import java.time.Duration;
import lombok.Builder;

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
