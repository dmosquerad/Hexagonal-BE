package com.architecture.hexagonal.domain.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidValueExceptionTest {

  @Test
  void constructor_shouldCreateExceptionWithMessage() {
    final InvalidValueException result =
        new InvalidValueException(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE);

    Assertions.assertThat(result).hasMessage(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE);
  }
}
