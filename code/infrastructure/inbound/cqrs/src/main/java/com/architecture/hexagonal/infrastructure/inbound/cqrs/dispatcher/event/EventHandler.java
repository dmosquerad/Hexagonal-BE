package com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.event;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface EventHandler<E> {

  void handle(E event) throws DomainException;
}
