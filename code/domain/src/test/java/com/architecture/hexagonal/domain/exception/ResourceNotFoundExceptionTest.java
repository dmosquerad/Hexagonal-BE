package com.architecture.hexagonal.domain.exception;

import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

  @Test
  void constructorShouldCreateExceptionWithMessage() {
    final ResourceNotFoundException result =
        new ResourceNotFoundException(
            ExceptionMessage.NOT_FOUND_DATA_MESSAGE, FailureType.RETRYABLE);

    Assertions.assertThat(result).hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE);
  }

  @Test
  void constructorShouldSetFailureType() {
    final ResourceNotFoundException result =
        new ResourceNotFoundException(
            ExceptionMessage.NOT_FOUND_DATA_MESSAGE, FailureType.NON_RETRYABLE);

    Assertions.assertThat(result.getFailureType()).isEqualTo(FailureType.NON_RETRYABLE);
  }
}
