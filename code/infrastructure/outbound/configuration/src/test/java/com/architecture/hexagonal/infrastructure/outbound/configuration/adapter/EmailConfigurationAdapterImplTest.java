package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailConfigurationAdapterImplTest {

  @InjectMocks EmailConfigurationAdapterImpl emailConfigurationAdapterImpl;

  @Mock EmailBlockConfig emailBlockConfig;

  @Test
  void getBlockedRules_shouldReturnBlockRulesVo_whenConfigIsProvided() {
    final EmailBlockRulesVo expected =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    Mockito.when(emailBlockConfig.getEmail()).thenReturn(Set.of("blocked@example.com"));
    Mockito.when(emailBlockConfig.getHost()).thenReturn(Set.of("blocked"));
    Mockito.when(emailBlockConfig.getTld()).thenReturn(Set.of("xyz"));
    Mockito.when(emailBlockConfig.getDomain()).thenReturn(Set.of("blocked.xyz"));
    Mockito.when(emailBlockConfig.getUsername()).thenReturn(Set.of("spammer"));

    final EmailBlockRulesVo result = emailConfigurationAdapterImpl.getBlockedRules();

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    Mockito.verify(emailBlockConfig).getEmail();
    Mockito.verify(emailBlockConfig).getHost();
    Mockito.verify(emailBlockConfig).getTld();
    Mockito.verify(emailBlockConfig).getDomain();
    Mockito.verify(emailBlockConfig).getUsername();
  }
}
