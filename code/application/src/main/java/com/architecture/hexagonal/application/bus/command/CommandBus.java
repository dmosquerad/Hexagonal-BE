package com.architecture.hexagonal.application.bus.command;

public interface CommandBus {
  <C, R> R execute(C command) throws Exception;
}
