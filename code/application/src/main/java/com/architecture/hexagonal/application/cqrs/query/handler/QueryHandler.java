package com.architecture.hexagonal.application.cqrs.query.handler;

public interface QueryHandler<Q, R> {

  Class<Q> getQueryType();

  R handle(Q query) throws Exception;
}
