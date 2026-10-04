package com.architecture.hexagonal.domain.model.vo.email;

import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailVoTestDataBuilder;
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
}
