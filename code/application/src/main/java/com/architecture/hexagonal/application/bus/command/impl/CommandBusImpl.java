package com.architecture.hexagonal.application.bus.command.impl;

import com.architecture.hexagonal.application.bus.command.CommandBus;
import com.architecture.hexagonal.application.bus.command.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    return handler.handle(command);
  }
}
