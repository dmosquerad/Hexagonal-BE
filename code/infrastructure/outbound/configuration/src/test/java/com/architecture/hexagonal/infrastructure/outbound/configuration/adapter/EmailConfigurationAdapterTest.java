package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
class EmailConfigurationAdapterTest {

  @InjectMocks
  EmailConfigurationAdapter emailConfigurationAdapter;

  @Mock
  EmailBlockConfig emailBlockConfig;

  @Test
  void filterBlocked_shouldReturnEmpty_whenNullEmails() {
    AssertionsForClassTypes.assertThat(emailConfigurationAdapter.filterBlocked(null))
        .isEqualTo(Set.of());
  }

  @Test
  void filterAllowed_shouldReturnEmpty_whenEmptyEmails() {
    AssertionsForClassTypes.assertThat(emailConfigurationAdapter.filterAllowed(Set.of()))
        .isEqualTo(Set.of());
  }

  @Test
  void filterBlocked_shouldReturnBlockedByHost_whenUsernameMatches() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    Mockito.when(emailBlockConfig.getUsername()).thenReturn(List.of("test"));

    final Set<EmailVo> result = emailConfigurationAdapter.filterBlocked(Set.of(emailVo));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(Set.of(emailVo));

    Mockito.verify(emailBlockConfig).getEmail();
    Mockito.verify(emailBlockConfig).getUsername();
    Mockito.verify(emailBlockConfig).getDomain();
    Mockito.verify(emailBlockConfig).getHost();
    Mockito.verify(emailBlockConfig).getTld();
  }

  @Test
  void filterAllowed_shouldReturnNotBlocked_whenNoBlockedMatch() {
    final EmailVo allowedVo = EmailVoTestDataBuilder.builder().build().emailVo();

    Mockito.when(emailBlockConfig.getHost()).thenReturn(List.of("test"));

    final Set<EmailVo> result = emailConfigurationAdapter.filterAllowed(Set.of(allowedVo));

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(Set.of(allowedVo));

    Mockito.verify(emailBlockConfig).getEmail();
    Mockito.verify(emailBlockConfig).getUsername();
    Mockito.verify(emailBlockConfig).getDomain();
    Mockito.verify(emailBlockConfig).getHost();
    Mockito.verify(emailBlockConfig).getTld();
  }

  @Test
  void filterAllowed_shouldExcludeBlockedHost_whenBlockedMatch() {
    final EmailVo blockedVo = EmailVoTestDataBuilder.builder().build().emailVo();

    Mockito.when(emailBlockConfig.getUsername()).thenReturn(List.of("test"));

    final Set<EmailVo> result = emailConfigurationAdapter.filterAllowed(Set.of(blockedVo));

    AssertionsForClassTypes.assertThat(result)
        .isEqualTo(Set.of());

    Mockito.verify(emailBlockConfig).getEmail();
    Mockito.verify(emailBlockConfig).getUsername();
    Mockito.verify(emailBlockConfig).getDomain();
    Mockito.verify(emailBlockConfig).getHost();
    Mockito.verify(emailBlockConfig).getTld();
  }

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
