package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
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
  void getBlockedRulesShouldReturnBlockRulesVoWhenConfigIsProvided() {
    final EmailBlockRulesVo expected =
        EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("blocked@example.com"))
            .host(Set.of("blocked"))
            .tld(Set.of("xyz"))
            .domain(Set.of("blocked.xyz"))
            .username(Set.of("spammer"))
            .build()
            .emailBlockRulesVo();

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
