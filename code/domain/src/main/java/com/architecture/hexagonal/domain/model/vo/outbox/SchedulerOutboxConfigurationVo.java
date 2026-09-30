package com.architecture.hexagonal.domain.model.vo.outbox;

import java.time.Duration;
import lombok.Builder;

@Builder
public record SchedulerOutboxConfigurationVo(Duration pollingInterval, int maxRetries) {}
