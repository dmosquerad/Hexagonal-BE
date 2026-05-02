package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailConfigurationAdapterTest {

  @InjectMocks
  EmailConfigurationAdapter emailConfigurationAdapter;

  @Mock
  EmailBlockConfig emailBlockConfig;

  @Test
  void getBlockedRules_shouldReturnBlockRulesVo_whenConfigIsProvided() {
    final EmailBlockRulesVo expected = EmailBlockRulesVo.builder()
        .email(List.of("blocked@example.com"))
        .host(List.of("blocked"))
        .tld(List.of("xyz"))
        .domain(List.of("blocked.xyz"))
        .username(List.of("spammer"))
        .build();

    Mockito.when(emailBlockConfig.getEmail()).thenReturn(List.of("blocked@example.com"));
    Mockito.when(emailBlockConfig.getHost()).thenReturn(List.of("blocked"));
    Mockito.when(emailBlockConfig.getTld()).thenReturn(List.of("xyz"));
    Mockito.when(emailBlockConfig.getDomain()).thenReturn(List.of("blocked.xyz"));
    Mockito.when(emailBlockConfig.getUsername()).thenReturn(List.of("spammer"));

    final EmailBlockRulesVo result = emailConfigurationAdapter.getBlockedRules();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expected);

    Mockito.verify(emailBlockConfig).getEmail();
    Mockito.verify(emailBlockConfig).getHost();
    Mockito.verify(emailBlockConfig).getTld();
    Mockito.verify(emailBlockConfig).getDomain();
    Mockito.verify(emailBlockConfig).getUsername();
  }
}
