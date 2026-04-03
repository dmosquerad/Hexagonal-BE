package com.architecture.hexagonal.application.bus.query;

public interface QueryBus {
  <Q, R> R execute(Q query) throws Exception;
}
