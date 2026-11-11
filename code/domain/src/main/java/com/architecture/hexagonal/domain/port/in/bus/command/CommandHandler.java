package com.architecture.hexagonal.domain.port.in.bus.command;

import lombok.NonNull;

public interface CommandHandler<C, R> {

  R handle(@NonNull C command);
}
