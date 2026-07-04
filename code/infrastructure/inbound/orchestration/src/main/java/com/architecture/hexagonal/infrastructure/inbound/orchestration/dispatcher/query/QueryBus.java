package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query;

public interface QueryBus {
  <Q, R> R execute(Q query);
}
