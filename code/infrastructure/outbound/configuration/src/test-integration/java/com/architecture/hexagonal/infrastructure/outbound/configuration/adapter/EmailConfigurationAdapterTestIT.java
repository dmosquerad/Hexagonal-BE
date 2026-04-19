package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo.EmailVoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Set;

@SpringBootTest(classes = EmailConfigurationAdapter.class)
@ContextConfiguration(classes = TestApplication.class)
class EmailConfigurationAdapterTestIT {

  @Autowired
  EmailConfigurationAdapter emailConfigurationAdapter;

  @MockitoSpyBean
  EmailBlockConfig emailBlockConfig;

  @Test
  void filterBlocked_shouldRespectConfiguredBlockedHost_whenHostIsBlocked() {
    final EmailVo blocked = EmailVoTestDataBuilder.builder().build().emailVo();

    final Set<EmailVo> result = emailConfigurationAdapter.filterBlocked(Set.of(blocked));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(Set.of(blocked));
  }

  @Test
  void filterAllowed_shouldIncludeGenericHost_whenHostIsNotConfiguredAsBlocked() {
    final EmailVo normal = EmailVoTestDataBuilder.builder().username("pepe").build().emailVo();

    final Set<EmailVo> result = emailConfigurationAdapter.filterAllowed(Set.of(normal));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(Set.of(normal));
  }
}
