package com.architecture.hexagonal.application.common.cqrs.query;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface QueryHandler<Q, R> {

  R handle(Q query) throws DomainException;
}
