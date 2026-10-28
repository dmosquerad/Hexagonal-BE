package com.architecture.hexagonal.domain.model.vo.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusVo {
  PENDING("PENDING"),
  PROCESSING("PROCESSING"),
  PROCESSED("PROCESSED"),
  BLOCKED("BLOCKED"),
  FAILED("FAILED"),
  DEAD_LETTER("DEAD_LETTER");

  private final String value;
}
