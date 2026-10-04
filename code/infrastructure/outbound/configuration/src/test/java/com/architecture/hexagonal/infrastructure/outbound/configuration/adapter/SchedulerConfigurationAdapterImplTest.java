package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerInboxConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerOutboxConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.model.vo.inbox.SchedulerInboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.model.vo.outbox.SchedulerOutboxConfigurationVoTestDataBuilder;
import java.time.Duration;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchedulerConfigurationAdapterImplTest {

  @InjectMocks SchedulerConfigurationAdapterImpl schedulerConfigurationAdapterImpl;

  @Mock SchedulerOutboxConfig schedulerOutboxConfig;

  @Mock SchedulerInboxConfig schedulerInboxConfig;

  @Test
  void getOutboxSchedulerShouldReturnConfigurationWhenConfigIsProvided() {
    final SchedulerOutboxConfigurationVo expected =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerOutboxConfig.getPollingInterval()).thenReturn(Duration.ofMinutes(1));
    Mockito.when(schedulerOutboxConfig.getMaxRetries()).thenReturn(5);

    final SchedulerOutboxConfigurationVo result =
        schedulerConfigurationAdapterImpl.getOutboxScheduler();

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    Mockito.verify(schedulerOutboxConfig).getPollingInterval();
    Mockito.verify(schedulerOutboxConfig).getMaxRetries();
  }

  @Test
  void getInboxSchedulerShouldReturnConfigurationWhenConfigIsProvided() {
    final SchedulerInboxConfigurationVo expected =
        SchedulerInboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerInboxConfigurationVo();
    Mockito.when(schedulerInboxConfig.getPollingInterval()).thenReturn(Duration.ofMinutes(1));
    Mockito.when(schedulerInboxConfig.getMaxRetries()).thenReturn(5);

    final SchedulerInboxConfigurationVo result =
        schedulerConfigurationAdapterImpl.getInboxScheduler();

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    Mockito.verify(schedulerInboxConfig).getPollingInterval();
    Mockito.verify(schedulerInboxConfig).getMaxRetries();
  }
}
