package com.architecture.hexagonal.domain.exception;

import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import lombok.NonNull;

public class InvalidValueException extends DomainException {

  public InvalidValueException(
      @NonNull final String message, @NonNull final FailureType failureType) {
    super(message, failureType);
  }
}
