package com.architecture.hexagonal.infrastructure.inbound.rabbitmq.command;

import com.architecture.hexagonal.application.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.config.RabbitQueuesProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommandBusImpl implements CommandBus {

  private static final String COMMAND_QUEUE_PREFIX = "command-";
  private static final String COMMAND_BINDING_PREFIX = "command.";

  private final RabbitTemplate rabbitTemplate;
  private final RabbitQueuesProperties rabbitQueuesProperties;

  public CommandBusImpl(final RabbitTemplate rabbitTemplate,
                        final RabbitQueuesProperties rabbitQueuesProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitQueuesProperties = rabbitQueuesProperties;
  }

  public static String defaultQueueNameFor(final Class<?> commandType) {
    return COMMAND_QUEUE_PREFIX + commandType.getSimpleName();
  }

  public static String defaultRoutingKeyFor(final Class<?> commandType) {
    return COMMAND_BINDING_PREFIX + commandType.getSimpleName();
  }

  public String queueNameFor(final Class<?> commandType) {
    return rabbitQueuesProperties.getCommandQueueName(
        commandType.getSimpleName(),
        defaultQueueNameFor(commandType));
  }

  public String routingKeyFor(final Class<?> commandType) {
    return rabbitQueuesProperties.getCommandBinding(
        commandType.getSimpleName(),
        defaultRoutingKeyFor(commandType));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C, R> R execute(final C command) throws Exception {
    final String routingKey = routingKeyFor(command.getClass());
    final Object reply = rabbitTemplate.convertSendAndReceive(
        rabbitQueuesProperties.getExchange(), routingKey, command);
    if (reply == null) {
      throw new IllegalStateException("No reply received for command: " + command.getClass());
    }
    return (R) reply;
  }
}
