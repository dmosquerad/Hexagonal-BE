package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command;

public interface CommandBus {
  <C, R> R execute(C command);
}
