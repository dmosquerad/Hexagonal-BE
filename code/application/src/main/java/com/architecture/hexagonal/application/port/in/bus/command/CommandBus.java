package com.architecture.hexagonal.application.port.in.bus.command;

import lombok.NonNull;

public interface CommandBus {

  <C, R> R execute(@NonNull C command);
}
