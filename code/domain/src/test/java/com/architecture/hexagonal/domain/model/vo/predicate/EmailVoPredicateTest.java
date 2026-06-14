package com.architecture.hexagonal.domain.model.vo.predicate;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoPredicateTest {

  @Test
  void isValidMail_shouldReturnTrue_whenEmailIsValid() {
    boolean result = EmailVoPredicate.IS_VALID_MAIL.test("user@example.com");

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void isValidMail_shouldReturnFalse_whenEmailIsInvalid() {
    boolean result = EmailVoPredicate.IS_VALID_MAIL.test("invalid-email");

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void canFormEmail_shouldReturnTrue_whenAllFieldsPresent() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void canFormEmail_shouldReturnFalse_whenFieldsMissing() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().username(null).build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo);

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void hostEquals_shouldReturnTrue_whenHostMatches() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.hostEquals("example").test(emailVo);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void hostEquals_shouldReturnFalse_whenHostDoesNotMatch() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.hostEquals("host").test(emailVo);

    Assertions.assertThat(result).isFalse();
  }

  @Test
  void canFormDomain_shouldReturnTrue_whenHostAndTldPresent() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo);

    Assertions.assertThat(result).isTrue();
  }

  @Test
  void canFormDomain_shouldReturnFalse_whenHostIsBlank() {
    EmailVo emailVo = EmailVoTestDataBuilder.builder().host(null).build().emailVo();

    boolean result = EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo);

    Assertions.assertThat(result).isFalse();
  }
}
