package com.architecture.hexagonal.domain.testutils.data.model.vo.inbox;

import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import java.time.Duration;
import lombok.Builder;

@Builder
public class SchedulerInboxConfigurationVoTestDataBuilder {

  @Builder.Default
  private Duration pollingInterval = Duration.parse("PT1M");

  @Builder.Default
  private int maxRetries = 5;

  public SchedulerInboxConfigurationVo schedulerInboxConfigurationVo() {
    return SchedulerInboxConfigurationVo.builder()
        .pollingInterval(pollingInterval)
        .maxRetries(maxRetries)
        .build();
  }
}
