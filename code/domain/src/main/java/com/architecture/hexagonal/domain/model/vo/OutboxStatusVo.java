package com.architecture.hexagonal.domain.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutboxStatusVo {
  PENDING("PENDING"),
  PROCESSING("PROCESSING"),
  PUBLISHED("PUBLISHED"),
  BLOCKED("BLOCKED"),
  FAILED("FAILED"),
  DEAD_LETTER("DEAD_LETTER");

  private final String value;
}
