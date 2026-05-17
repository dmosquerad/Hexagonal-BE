package com.architecture.hexagonal.infrastructure.cqrs.bus.event;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface EventBus {

  <E> void publish(E event) throws DomainException;
}
