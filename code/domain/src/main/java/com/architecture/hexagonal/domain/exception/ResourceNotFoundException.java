package com.architecture.hexagonal.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceNotFoundException extends DomainException {

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}