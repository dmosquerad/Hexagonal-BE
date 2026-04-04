package com.architecture.hexagonal.infrastructure.inbound.rabbitmq.command;

import com.architecture.hexagonal.application.handler.command.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CommandBusListener {

  private final Map<Class<?>, CommandHandler<?, ?>> handlersByCommand = new HashMap<>();

  public CommandBusListener(final List<CommandHandler<?, ?>> commandHandlers) {
    commandHandlers.forEach(handler -> handlersByCommand.put(handler.getCommandType(), handler));
  }

  @RabbitListener(queues = "#{commandQueueNames}")
  public Object onCommand(final Object command) throws Exception {
    final CommandHandler handler = handlersByCommand.get(command.getClass());
    if (Objects.isNull(handler)) {
      throw new IllegalStateException("No command handler registered for command: " + command.getClass());
    }
    return handler.handle(command);
  }
}
