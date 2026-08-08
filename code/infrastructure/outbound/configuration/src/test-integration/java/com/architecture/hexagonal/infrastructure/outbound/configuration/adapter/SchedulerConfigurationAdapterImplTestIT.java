package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.inbox.SchedulerInboxConfigurationVo;
import com.architecture.hexagonal.domain.model.vo.outbox.SchedulerOutboxConfigurationVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.SchedulerOutboxConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.model.vo.inbox.SchedulerInboxConfigurationVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.model.vo.outbox.SchedulerOutboxConfigurationVoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = SchedulerConfigurationAdapterImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class SchedulerConfigurationAdapterImplTestIT {

  @Autowired
  SchedulerConfigurationAdapterImpl schedulerConfigurationAdapterImpl;

  @MockitoSpyBean
  SchedulerOutboxConfig schedulerOutboxConfig;

  @MockitoSpyBean
  SchedulerOutboxConfig schedulerInboxConfig;

  @Test
  void getOutbox_Scheduler_shouldReturnConfigurationFromConfig() {
      final SchedulerOutboxConfigurationVo expected = SchedulerOutboxConfigurationVoTestDataBuilder
              .builder()
              .build()
              .schedulerOutboxConfigurationVo();
    final SchedulerOutboxConfigurationVo result = schedulerConfigurationAdapterImpl.getOutboxScheduler();

    AssertionsForClassTypes.assertThat(result).isEqualTo(expected);
  }

  @Test
  void getInbox_Scheduler_shouldReturnConfigurationFromConfig() {
      final SchedulerInboxConfigurationVo expected = SchedulerInboxConfigurationVoTestDataBuilder
                .builder()
                .build()
                .schedulerInboxConfigurationVo();
      final SchedulerInboxConfigurationVo result = schedulerConfigurationAdapterImpl.getInboxScheduler();

      AssertionsForClassTypes.assertThat(result).isEqualTo(expected);
    }
}
