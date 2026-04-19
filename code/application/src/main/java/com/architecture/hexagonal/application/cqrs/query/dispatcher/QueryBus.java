package com.architecture.hexagonal.application.cqrs.query.dispatcher;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface QueryBus {
  <Q, R> R execute(Q query) throws DomainException;
}
