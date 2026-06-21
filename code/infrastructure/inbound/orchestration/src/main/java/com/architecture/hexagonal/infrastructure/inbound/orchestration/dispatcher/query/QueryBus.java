package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface QueryBus {
  <Q, R> R execute(Q query) throws DomainException;
}
