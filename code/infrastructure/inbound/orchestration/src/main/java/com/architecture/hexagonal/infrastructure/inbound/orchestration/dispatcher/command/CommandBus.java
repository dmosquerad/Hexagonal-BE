package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface CommandBus {
  <C, R> R execute(C command) throws DomainException;
}
