package com.architecture.hexagonal.application.port.in.bus.command;

import com.architecture.hexagonal.application.port.in.bus.MessageHandler;
import lombok.NonNull;

public interface CommandHandler<C, R> extends MessageHandler {

  R handle(@NonNull C command);
}
