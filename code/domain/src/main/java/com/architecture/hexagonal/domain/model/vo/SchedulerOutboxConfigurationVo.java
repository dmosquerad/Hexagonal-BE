package com.architecture.hexagonal.domain.model.vo;

import java.time.Duration;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SchedulerOutboxConfigurationVo {
  Duration pollingInterval;
  int maxRetries;
}
