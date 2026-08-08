package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command;

import lombok.NonNull;

public interface CommandHandler<C, R> {

  R handle(@NonNull C command);
}
