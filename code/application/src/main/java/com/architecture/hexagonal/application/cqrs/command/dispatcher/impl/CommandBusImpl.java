package com.architecture.hexagonal.application.cqrs.command.dispatcher.impl;

import com.architecture.hexagonal.application.cqrs.command.dispatcher.CommandBus;
import com.architecture.hexagonal.application.cqrs.command.handler.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class CommandBusImpl implements CommandBus {

  private final List<CommandHandler<?, ?>> commandHandlers;

  private final Map<Class<?>, CommandHandler<?, ?>> handlersByCommand = new HashMap<>();

  public CommandBusImpl(final List<CommandHandler<?, ?>> commandHandlers) {
    this.commandHandlers = commandHandlers;
    this.commandHandlers.forEach(handler -> handlersByCommand.put(handler.getCommandType(), handler));
  }

  @Override
  public <C, R> R execute(final C command) throws Exception {
    final CommandHandler<C, R> handler =
            (CommandHandler<C, R>) handlersByCommand.get(command.getClass());

    if (Objects.isNull(handler)) {
      throw new IllegalStateException("No handler found for command: " + command.getClass());
    }
    
    return handler.handle(command);
  }
}
