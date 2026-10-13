package com.architecture.hexagonal.application.service.business.email;

import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.service.business.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

  @InjectMocks EmailServiceImpl emailService;

  @Mock EmailConfigurationPort emailConfigurationPort;

  @Test
  void executeShouldReturnBlockedRulesWhenConfigurationIsAvailable() {
    final EmailBlockRulesVo emailBlockRulesVo =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    Mockito.when(emailConfigurationPort.getBlockedRules()).thenReturn(emailBlockRulesVo);

    final EmailBlockRulesVo result = emailService.getBlockedRules();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(emailBlockRulesVo);

    Mockito.verify(emailConfigurationPort).getBlockedRules();
  }

  @Test
  void isEmailBlockedShouldReturnFalseWhenEmailIsAllowed() {
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo());

    final boolean result = emailService.isEmailBlocked(EmailVoFactory.from("test@example.com"));

    AssertionsForClassTypes.assertThat(result).isFalse();
    Mockito.verify(emailConfigurationPort).getBlockedRules();
  }

  @Test
  void isEmailBlockedShouldReturnTrueWhenEmailIsBlocked() {
    final String email = "test@example.com";
    Mockito.when(emailConfigurationPort.getBlockedRules())
        .thenReturn(
            EmailBlockRulesVoTestDataBuilder.builder()
                .email(Set.of(email))
                .build()
                .emailBlockRulesVo());

    final boolean result = emailService.isEmailBlocked(EmailVoFactory.from(email));

    AssertionsForClassTypes.assertThat(result).isTrue();
    Mockito.verify(emailConfigurationPort).getBlockedRules();
  }
}
