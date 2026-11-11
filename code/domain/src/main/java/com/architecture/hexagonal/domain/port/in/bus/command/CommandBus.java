package com.architecture.hexagonal.domain.port.in.bus.command;

import lombok.NonNull;

public interface CommandBus {

  <C, R> R execute(@NonNull C command);
}
