package com.architecture.hexagonal.domain.model.vo.email.predicate;

import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoPredicateTest {

  @Test
  void isValidMailShouldReturnTrueWhenEmailIsValid() {
    boolean result = EmailVoPredicate.IS_VALID_MAIL.test("user@example.com");

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void isValidMailShouldReturnFalseWhenEmailIsInvalid() {
    boolean result = EmailVoPredicate.IS_VALID_MAIL.test("invalid-email");

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void canFormEmailShouldReturnTrueWhenAllFieldsPresent() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void canFormEmailShouldReturnFalseWhenFieldsMissing() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().username(null).build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo);

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void hostEqualsShouldReturnTrueWhenHostMatches() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.hostEquals("example").test(emailVo);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void hostEqualsShouldReturnFalseWhenHostDoesNotMatch() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.hostEquals("host").test(emailVo);

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void canFormDomainShouldReturnTrueWhenHostAndTldPresent() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void canFormDomainShouldReturnFalseWhenHostIsBlank() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().host(null).build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo);

    Assertions.assertThat(result).isFalse();
  }
}
