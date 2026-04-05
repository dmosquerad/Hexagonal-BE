package com.architecture.hexagonal.application.cqrs.command.dispatcher;

public interface CommandBus {
  <C, R> R execute(C command) throws Exception;
}
