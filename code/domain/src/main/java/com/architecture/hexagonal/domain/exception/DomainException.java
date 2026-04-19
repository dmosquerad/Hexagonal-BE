package com.architecture.hexagonal.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DomainException extends Exception {

  public DomainException(final String message) {
    super(message);
  }
}
