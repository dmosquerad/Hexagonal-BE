package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query;

public interface QueryHandler<Q, R> {

  R handle(Q query);
}
