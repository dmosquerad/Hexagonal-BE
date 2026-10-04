package com.architecture.hexagonal.domain.model.aggregate;

public interface AggregateRoot<R> {
  R getId();
}
