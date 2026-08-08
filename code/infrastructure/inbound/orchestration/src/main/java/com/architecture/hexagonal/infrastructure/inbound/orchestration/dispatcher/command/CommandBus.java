package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command;

import lombok.NonNull;

public interface CommandBus {
  <C, R> R execute(@NonNull C command);
}
