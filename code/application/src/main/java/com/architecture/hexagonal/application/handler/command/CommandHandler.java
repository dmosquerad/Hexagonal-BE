package com.architecture.hexagonal.application.handler.command;

public interface CommandHandler<C, R> {

  Class<C> getCommandType();

  R handle(C command) throws Exception;
}
