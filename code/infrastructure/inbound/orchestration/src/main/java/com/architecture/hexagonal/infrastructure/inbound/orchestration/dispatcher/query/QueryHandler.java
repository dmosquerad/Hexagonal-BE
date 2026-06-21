package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface QueryHandler<Q, R> {

  R handle(Q query) throws DomainException;
}
