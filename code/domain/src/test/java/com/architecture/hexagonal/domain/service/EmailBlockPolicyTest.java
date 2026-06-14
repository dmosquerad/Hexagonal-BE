package com.architecture.hexagonal.domain.service;

import com.architecture.hexagonal.domain.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

class EmailBlockPolicyTest {

  @Test
  void isBlocked_shouldReturnFalse_whenRulesAreEmpty() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo()))
        .isFalse();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenEmailMatchesBlockedEmail() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .email(Set.of("test@example.com"))
                    .build()
                    .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenUsernameMatchesBlockedUsername() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .username(Set.of("test"))
                    .build()
                    .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenHostMatchesBlockedHost() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .host(Set.of("example"))
                    .build()
                    .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenTldMatchesBlockedTld() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .tld(Set.of("com"))
                    .build()
                    .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenDomainMatchesBlockedDomain() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .domain(Set.of("example.com"))
                    .build()
                    .emailBlockRulesVo()))
        .isTrue();
  }

  @Test
  void isBlocked_shouldReturnFalse_whenUsernameCannotFormEmail() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().username("").build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .email(Set.of("test@example.com"))
                    .build()
                    .emailBlockRulesVo()))
        .isFalse();
  }

  @Test
  void isBlocked_shouldReturnFalse_whenHostCannotFormDomain() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder().host("").build().emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .host(Set.of("example"))
                    .build()
                    .emailBlockRulesVo()))
        .isFalse();
  }

  @Test
  void isBlocked_shouldReturnTrue_whenBlockedEmailMatchesCaseInsensitively() {
    AssertionsForClassTypes.assertThat(
            EmailBlockPolicy.isBlocked(
                EmailVoTestDataBuilder.builder()
                    .username("Test")
                    .host("Example")
                    .tld("Com")
                    .build()
                    .emailVo(),
                EmailBlockRulesVoTestDataBuilder.builder()
                    .email(Set.of("test@example.com"))
                    .build()
                    .emailBlockRulesVo()))
        .isTrue();
  }
}
