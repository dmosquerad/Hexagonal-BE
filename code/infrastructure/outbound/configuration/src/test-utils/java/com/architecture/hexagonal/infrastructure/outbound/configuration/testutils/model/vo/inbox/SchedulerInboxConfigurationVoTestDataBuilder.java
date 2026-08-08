package com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.model.vo.inbox;

import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import lombok.Builder;

import java.time.Duration;

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