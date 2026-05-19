package com.architecture.hexagonal.application.common.cqrs.command;

import com.architecture.hexagonal.domain.exception.DomainException;

public interface CommandHandler<C, R> {

  R handle(C command) throws DomainException;
}
