package com.architecture.hexagonal.domain.model.vo.email;

import com.architecture.hexagonal.domain.databuilder.model.vo.email.EmailVoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailVoTest {

  @Test
  void getEmailShouldReturnEmailWhenEmailIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = emailVo.getEmail();

    Assertions.assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void getEmailShouldReturnEmptyWhenEmailIsInvalid() {
    final EmailVo emailVo =
        EmailVoTestDataBuilder.builder().username(null).host(null).tld(null).build().emailVo();

    final String result = emailVo.getEmail();

    Assertions.assertThat(result).isEmpty();
  }

  @Test
  void getDomainShouldReturnDomainWhenEmailIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = emailVo.getDomain();

    Assertions.assertThat(result).isEqualTo("example.com");
  }

  @Test
  void getDomainShouldReturnEmptyWhenEmailIsInvalid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().host(null).tld(null).build().emailVo();

    final String result = emailVo.getDomain();

    Assertions.assertThat(result).isEmpty();
  }

  @Test
  void canFormEmailShouldReturnTrueWhenAllFieldsArePresent() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    Assertions.assertThat(emailVo.canFormEmail()).isTrue();
  }

  @Test
  void canFormEmailShouldReturnFalseWhenAFieldIsMissing() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().username(null).build().emailVo();

    Assertions.assertThat(emailVo.canFormEmail()).isFalse();
  }

  @Test
  void canFormDomainShouldReturnTrueWhenHostAndTldArePresent() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    Assertions.assertThat(emailVo.canFormDomain()).isTrue();
  }

  @Test
  void canFormDomainShouldReturnFalseWhenHostIsMissing() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().host(null).build().emailVo();

    Assertions.assertThat(emailVo.canFormDomain()).isFalse();
  }
}
