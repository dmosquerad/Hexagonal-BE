package com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.event;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface EventBus {

  <E> void publish(E event) throws DomainException;
}
