package com.architecture.hexagonal.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceNotFoundException extends Exception {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}