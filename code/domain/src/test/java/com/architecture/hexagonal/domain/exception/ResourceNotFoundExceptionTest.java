package com.architecture.hexagonal.domain.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

  @Test
  void constructor_shouldCreateExceptionWithMessage() {
    final ResourceNotFoundException result =
        new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE);

    Assertions.assertThat(result).hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE);
  }
}
