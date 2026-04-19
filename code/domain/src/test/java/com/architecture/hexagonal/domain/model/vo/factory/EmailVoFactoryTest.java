package com.architecture.hexagonal.domain.model.vo.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;

class EmailVoFactoryTest {

  @Test
  void from_shouldReturnEmailVo_whenEmailIsValid() {
    final EmailVo expected = EmailVoTestDataBuilder.builder().build().emailVo();

    final EmailVo result = EmailVoFactory.from("test@example.com");

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void from_shouldThrowIllegalArgumentException_whenEmailIsInvalid() {
    assertThatThrownBy(() -> EmailVoFactory.from("not-an-email"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(ExceptionMessage.INVALID_EMAIL_FORMAT);
  }
}