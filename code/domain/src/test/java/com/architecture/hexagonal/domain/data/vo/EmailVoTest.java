package com.architecture.hexagonal.domain.data.vo;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class EmailVoTest {

  @Test
  void canFormEmailShouldReturnTrueWhenUsernameAndDomainArePresent() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    assertThat(emailVo.canFormEmail()).isTrue();
  }

  @Test
  void canFormEmailShouldReturnFalseWhenIsMissing() {
    final EmailVo dataMissing = EmailVoTestDataBuilder.builder()
      .username(null)
      .domain(null)
      .build()
      .emailVo();

    assertThat(dataMissing.canFormEmail()).isFalse();
  }

  @Test
  void getEmailShouldReturnEmail() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    final String result = emailVo.getEmail();

    assertThat(result).isEqualTo("test@example.com");
  }

  @Test
  void getEmailShouldReturnEmptyWhenDataIsInvalid() {
    final EmailVo emailVo = EmailVoTestDataBuilder.builder()
      .username(null)
      .domain(null)
      .build()
      .emailVo();

    final String result = emailVo.getEmail();

    assertThat(result).isEmpty();
  }
}