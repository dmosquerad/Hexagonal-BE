package com.architecture.hexagonal.application.handler.query;

public interface QueryHandler<Q, R> {

  Class<Q> getQueryType();

  R handle(Q query) throws Exception;
}
