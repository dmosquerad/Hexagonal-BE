package com.architecture.hexagonal.domain.model.vo.inbox;

import java.time.Duration;
import lombok.Builder;

@Builder
public record SchedulerInboxConfigurationVo(Duration pollingInterval, int maxRetries) {}
