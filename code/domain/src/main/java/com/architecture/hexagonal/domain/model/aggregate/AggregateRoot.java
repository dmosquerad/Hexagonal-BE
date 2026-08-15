package com.architecture.hexagonal.domain.model.aggregate;

public interface AggregateRoot<ROOT> {
  ROOT getId();
}
