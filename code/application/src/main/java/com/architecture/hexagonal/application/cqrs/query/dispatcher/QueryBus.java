package com.architecture.hexagonal.application.cqrs.query.dispatcher;

public interface QueryBus {
  <Q, R> R execute(Q query) throws Exception;
}
