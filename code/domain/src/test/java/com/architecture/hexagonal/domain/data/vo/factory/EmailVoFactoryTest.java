package com.architecture.hexagonal.domain.data.vo.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class EmailVoFactoryTest {

  @Test
  void fromShouldReturnEmailVoWhenEmailIsValid() {
    final EmailVo expected = EmailVoTestDataBuilder.builder()
      .build()
      .emailVo();

    final EmailVo result = EmailVoFactory.from("test@example.com");

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void fromShouldReturnEmptyEmailVoWhenEmailIsInvalid() {
    final EmailVo result = EmailVoFactory.from(null);

    assertThat(result).isEqualTo(EmailVoTestDataBuilder.builder()
        .username(null)
        .host(null)
        .tld(null)
        .build()
        .emailVo());
  }
}