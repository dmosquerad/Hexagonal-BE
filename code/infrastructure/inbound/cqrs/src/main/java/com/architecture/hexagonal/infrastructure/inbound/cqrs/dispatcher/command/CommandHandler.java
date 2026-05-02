package com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.command;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface CommandHandler<C, R> {

  R handle(C command) throws DomainException;
}
