package com.architecture.hexagonal.domain.exception;

import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class DomainException extends RuntimeException {
  private final FailureType failureType;

  public DomainException(@NonNull final String message, @NonNull final FailureType failureType) {
    super(message);
    this.failureType = failureType;
  }
}
