package com.architecture.hexagonal.domain.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

  @Test
  void constructorShouldCreateExceptionWithMessage() {
    final ResourceNotFoundException result =
        new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE);

    Assertions.assertThat(result).hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE);
  }
}
