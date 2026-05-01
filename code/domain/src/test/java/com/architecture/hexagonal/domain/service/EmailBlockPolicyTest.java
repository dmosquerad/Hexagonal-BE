package com.architecture.hexagonal.domain.service;

import com.architecture.hexagonal.domain.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

class EmailBlockPolicyTest {

  @Test
  void isBlocked_shouldReturnFalse_whenRulesAreEmpty() {
    AssertionsForClassTypes.assertThat(EmailBlockPolicy.isBlocked(
        EmailVoTestDataBuilder.builder().build().emailVo(),
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo()))
        .isFalse();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenEmailMatchesBlockedEmail() {
    AssertionsForClassTypes.assertThat(EmailBlockPolicy.isBlocked(
        EmailVoTestDataBuilder.builder().build().emailVo(),
        EmailBlockRulesVoTestDataBuilder
          .builder()
          .email(List.of("test@example.com"))
          .build().emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenUsernameMatchesBlockedUsername() {
    AssertionsForClassTypes.assertThat(EmailBlockPolicy.isBlocked(
        EmailVoTestDataBuilder.builder().build().emailVo(),
        EmailBlockRulesVoTestDataBuilder
          .builder()
          .username(List.of("test"))
          .build().emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenHostMatchesBlockedHost() {
    AssertionsForClassTypes.assertThat(EmailBlockPolicy.isBlocked(
        EmailVoTestDataBuilder.builder().build().emailVo(),
        EmailBlockRulesVoTestDataBuilder
          .builder()
          .host(List.of("example"))
          .build()
          .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenTldMatchesBlockedTld() {
    AssertionsForClassTypes.assertThat(EmailBlockPolicy.isBlocked(
        EmailVoTestDataBuilder.builder().build().emailVo(),
        EmailBlockRulesVoTestDataBuilder
          .builder()
          .tld(List.of("com"))
          .build()
          .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenDomainMatchesBlockedDomain() {
    AssertionsForClassTypes.assertThat(EmailBlockPolicy.isBlocked(
        EmailVoTestDataBuilder.builder().build().emailVo(),
        EmailBlockRulesVoTestDataBuilder
          .builder()
          .domain(List.of("example.com"))
          .build()
          .emailBlockRulesVo()))
        .isTrue();
  }

}
