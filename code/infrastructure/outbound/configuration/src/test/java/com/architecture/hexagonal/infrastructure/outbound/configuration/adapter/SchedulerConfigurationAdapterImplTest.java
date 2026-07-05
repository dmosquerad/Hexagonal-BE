package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerOutboxConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo.SchedulerOutboxConfigurationVoTestDataBuilder;
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

  @Test
  void getSchedulerOutbox_shouldReturnConfiguration_whenConfigIsProvided() {
    final SchedulerOutboxConfigurationVo expected =
        SchedulerOutboxConfigurationVoTestDataBuilder.builder()
            .build()
            .schedulerOutboxConfigurationVo();

    Mockito.when(schedulerOutboxConfig.getPollingInterval()).thenReturn(Duration.ofMinutes(1));
    Mockito.when(schedulerOutboxConfig.getMaxRetries()).thenReturn(5);

    final SchedulerOutboxConfigurationVo result =
        schedulerConfigurationAdapterImpl.getSchedulerOutbox();

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    Mockito.verify(schedulerOutboxConfig).getPollingInterval();
    Mockito.verify(schedulerOutboxConfig).getMaxRetries();
  }
}
