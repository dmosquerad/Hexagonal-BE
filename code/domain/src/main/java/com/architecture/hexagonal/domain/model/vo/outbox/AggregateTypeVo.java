package com.architecture.hexagonal.domain.model.vo.outbox;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AggregateTypeVo {
  USER("USER");

  private final String aggregateType;
}
