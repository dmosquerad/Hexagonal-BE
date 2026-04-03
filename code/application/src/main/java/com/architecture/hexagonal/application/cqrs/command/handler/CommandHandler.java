package com.architecture.hexagonal.application.cqrs.command.handler;

public interface CommandHandler<C, R> {

  Class<C> getCommandType();

  R handle(C command) throws Exception;
}
