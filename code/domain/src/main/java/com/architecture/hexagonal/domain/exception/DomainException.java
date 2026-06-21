package com.architecture.hexagonal.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DomainException extends RuntimeException {

  public DomainException(final String message) {
    super(message);
  }
}
