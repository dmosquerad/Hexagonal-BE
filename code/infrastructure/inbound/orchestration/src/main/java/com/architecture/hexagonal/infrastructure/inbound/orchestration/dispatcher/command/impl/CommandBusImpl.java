package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.impl;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class CommandBusImpl implements CommandBus {

  private final Map<Class<?>, CommandHandler<?, ?>> handlersByCommand;

  public CommandBusImpl(final List<CommandHandler<?, ?>> commandHandlers) {
    this.handlersByCommand =
        commandHandlers.stream()
            .collect(
                Collectors.toMap(
                    handler ->
                        ResolvableType.forClass(handler.getClass())
                            .as(CommandHandler.class)
                            .getGeneric(0)
                            .resolve(),
                    handler -> handler));
  }

  @Override
  public <C, R> R execute(final C command) throws DomainException {
    final CommandHandler<C, R> handler =
        (CommandHandler<C, R>) handlersByCommand.get(command.getClass());

    if (Objects.isNull(handler)) {
      throw new IllegalStateException("No handler found for command: " + command.getClass());
    }

    return handler.handle(command);
  }
}
