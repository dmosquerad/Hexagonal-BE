package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.bus.MessageHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandBusImpl implements CommandBus {

  private final MessageHandlerFinder messageHandlerFinder;

  @Override
  public <C, R> R execute(final @NonNull C command) {
    final CommandHandler<C, R> handler = messageHandlerFinder.find(command, CommandHandler.class);
    return handler.handle(command);
  }
}
