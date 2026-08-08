package com.architecture.hexagonal.domain.model.aggregate;

public interface Aggregate<ROOT> {

  Object getId();

  ROOT getAggregateRoot();
}
