package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = EmailConfigurationAdapter.class)
@ContextConfiguration(classes = TestApplication.class)
class EmailConfigurationAdapterTestIT {

  @Autowired
  EmailConfigurationAdapter emailConfigurationAdapter;

  @MockitoSpyBean
  EmailBlockConfig emailBlockConfig;

  @Test
  void getBlockedRules_shouldReturnRulesFromConfig() {
    final EmailBlockRulesVo result = emailConfigurationAdapter.getBlockedRules();

    AssertionsForClassTypes.assertThat(result).isNotNull();
  }
}
