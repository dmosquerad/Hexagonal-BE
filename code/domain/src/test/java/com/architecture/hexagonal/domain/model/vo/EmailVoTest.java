package com.architecture.hexagonal.domain.model.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class EmailVoTest {

  @Test
  void getEmail_shouldReturnEmail_whenEmailIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = emailVo.getEmail();

    assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void getEmail_shouldReturnEmpty_whenEmailIsInvalid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
        .username(null)
        .host(null)
        .tld(null)
        .build()
        .emailVo();

    final String result = emailVo.getEmail();

    assertThat(result).isEmpty();
  }

  @Test
  void getDomain_shouldReturnDomain_whenEmailIsValid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();

    final String result = emailVo.getDomain();

    assertThat(result).isEqualTo("example.com");
  }

  @Test
  void getDomain_shouldReturnEmpty_whenEmailIsInvalid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
        .host(null)
        .tld(null)
        .build()
        .emailVo();

    final String result = emailVo.getDomain();

    assertThat(result).isEmpty();
  }
}