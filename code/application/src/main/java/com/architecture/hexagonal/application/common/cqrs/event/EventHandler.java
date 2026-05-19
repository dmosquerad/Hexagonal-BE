package com.architecture.hexagonal.application.common.cqrs.event;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface EventHandler<E> {

  void handle(E event) throws DomainException;
}
