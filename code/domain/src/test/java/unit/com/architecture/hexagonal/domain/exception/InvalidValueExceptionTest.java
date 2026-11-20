package com.architecture.hexagonal.domain.exception;

import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InvalidValueExceptionTest {

  @Test
  void constructorShouldCreateExceptionWithMessage() {
    final InvalidValueException result =
        new InvalidValueException(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE, FailureType.RETRYABLE);

    Assertions.assertThat(result).hasMessage(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE);
    Assertions.assertThat(result.getFailureType()).isEqualTo(FailureType.RETRYABLE);
  }

  @Test
  void constructorShouldSetFailureType() {
    final InvalidValueException result =
        new InvalidValueException(
            ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE, FailureType.NON_RETRYABLE);

    Assertions.assertThat(result).hasMessage(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE);
    Assertions.assertThat(result.getFailureType()).isEqualTo(FailureType.NON_RETRYABLE);
  }
}
