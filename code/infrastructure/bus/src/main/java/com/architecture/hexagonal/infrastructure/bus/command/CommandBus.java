package com.architecture.hexagonal.infrastructure.bus.command;

import lombok.NonNull;

public interface CommandBus {

  <C, R> R execute(@NonNull C command);
}
