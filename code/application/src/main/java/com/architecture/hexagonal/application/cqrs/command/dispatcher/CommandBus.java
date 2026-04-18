package com.architecture.hexagonal.application.cqrs.command.dispatcher;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface CommandBus {
  <C, R> R execute(C command) throws DomainException;
}
