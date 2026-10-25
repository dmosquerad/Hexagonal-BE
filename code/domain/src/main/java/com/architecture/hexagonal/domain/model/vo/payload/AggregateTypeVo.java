package com.architecture.hexagonal.domain.model.vo.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AggregateTypeVo {
  USER(Naming.AGGREGATE_TYPE_USER);

  private final String aggregateType;

  @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
  public static final class Naming {
    public static final String AGGREGATE_TYPE_USER = "USER";
  }
}
