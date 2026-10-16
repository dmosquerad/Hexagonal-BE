package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.bus.middleware.BusMiddleware;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandBusImpl implements CommandBus {

  private final BusHandlerFinder handlerFinder;
  private final BusMiddleware loggingMiddleware;

  @Override
  public <C, R> R execute(final @NonNull C command) {
    return loggingMiddleware.execute(
        command,
        () -> {
          final CommandHandler<C, R> handler = handlerFinder.find(command, CommandHandler.class);
          return handler.handle(command);
        });
  }
}
