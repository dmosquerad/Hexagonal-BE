package com.architecture.hexagonal.application.port.in.bus.command;

import lombok.NonNull;

public interface CommandHandler<C, R> {

  R handle(@NonNull C command);
}
