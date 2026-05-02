package com.architecture.hexagonal.domain.exception;

public class InvalidValueException extends DomainException {

  public InvalidValueException(final String message) {
    super(message);
  }
}
