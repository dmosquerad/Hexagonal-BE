package com.architecture.hexagonal.infrastructure.inbound.cqrs.bus.query;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface QueryBus {
  <Q, R> R execute(Q query) throws DomainException;
}
