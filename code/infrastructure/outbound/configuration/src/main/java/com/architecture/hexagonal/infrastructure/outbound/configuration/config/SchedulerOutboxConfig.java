package com.architecture.hexagonal.infrastructure.outbound.configuration.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "scheduler.outbox")
public class SchedulerOutboxConfig {
  private Duration pollingInterval;
  private int maxRetries;
}
