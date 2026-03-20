package com.architecture.hexagonal.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceNotFoundException extends Exception {

  public ResourceNotFoundException(final String message) {
    super(message);
  }
}