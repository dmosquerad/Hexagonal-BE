package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command;

public interface CommandHandler<C, R> {

  R handle(C command);
}
