package com.architecture.hexagonal.infrastructure.cqrs.bus.command;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface CommandBus {
  <C, R> R execute(C command) throws DomainException;
}
