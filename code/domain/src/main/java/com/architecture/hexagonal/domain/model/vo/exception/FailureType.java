package com.architecture.hexagonal.domain.model.vo.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FailureType {
  RETRYABLE,
  NON_RETRYABLE
}
