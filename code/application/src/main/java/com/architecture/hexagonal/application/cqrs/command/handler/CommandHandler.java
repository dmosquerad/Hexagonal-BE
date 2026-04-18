package com.architecture.hexagonal.application.cqrs.command.handler;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface CommandHandler<C, R> {

  R handle(C command) throws DomainException;
}
