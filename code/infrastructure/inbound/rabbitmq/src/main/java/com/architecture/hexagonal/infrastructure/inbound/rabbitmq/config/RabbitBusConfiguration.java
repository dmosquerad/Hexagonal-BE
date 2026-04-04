package com.architecture.hexagonal.infrastructure.inbound.rabbitmq.config;

import com.architecture.hexagonal.application.handler.command.CommandHandler;
import com.architecture.hexagonal.application.handler.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.command.CommandBusImpl;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.command.CommandBusListener;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.config.RabbitQueuesProperties;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.query.QueryBusImpl;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.query.QueryBusListener;
import java.util.List;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitQueuesProperties.class)
public class RabbitBusConfiguration {

  @Bean
  public Jackson2JsonMessageConverter rabbitMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public TopicExchange rabbitBusExchange(final RabbitQueuesProperties queueProperties) {
    return new TopicExchange(queueProperties.getExchange());
  }

  @Bean
  public String[] commandQueueNames(final List<CommandHandler<?, ?>> commandHandlers,
                                    final RabbitQueuesProperties queueProperties) {
    return commandHandlers.stream()
        .map(handler -> queueProperties.getCommandQueueName(
            handler.getCommandType().getSimpleName(),
            CommandBusImpl.defaultQueueNameFor(handler.getCommandType())))
        .toArray(String[]::new);
  }

  @Bean
  public String[] queryQueueNames(final List<QueryHandler<?, ?>> queryHandlers,
                                  final RabbitQueuesProperties queueProperties) {
    return queryHandlers.stream()
        .map(handler -> queueProperties.getQueryQueueName(
            handler.getQueryType().getSimpleName(),
            QueryBusImpl.defaultQueueNameFor(handler.getQueryType())))
        .toArray(String[]::new);
  }

  @Bean
  public CommandQueueRegistrar commandQueueRegistrar(final AmqpAdmin amqpAdmin,
                                                      final TopicExchange rabbitBusExchange,
                                                      final List<CommandHandler<?, ?>> commandHandlers,
                                                      final RabbitQueuesProperties queueProperties) {
    return new CommandQueueRegistrar(amqpAdmin, rabbitBusExchange, commandHandlers, queueProperties);
  }

  @Bean
  public QueryQueueRegistrar queryQueueRegistrar(final AmqpAdmin amqpAdmin,
                                                  final TopicExchange rabbitBusExchange,
                                                  final List<QueryHandler<?, ?>> queryHandlers,
                                                  final RabbitQueuesProperties queueProperties) {
    return new QueryQueueRegistrar(amqpAdmin, rabbitBusExchange, queryHandlers, queueProperties);
  }

  public static class CommandQueueRegistrar {

    public CommandQueueRegistrar(final AmqpAdmin amqpAdmin,
                                 final TopicExchange rabbitBusExchange,
                                 final List<CommandHandler<?, ?>> commandHandlers,
                                 final RabbitQueuesProperties queueProperties) {
      commandHandlers.stream()
          .forEach(handler -> {
            final String queueName = queueProperties.getCommandQueueName(
                handler.getCommandType().getSimpleName(),
                CommandBusImpl.defaultQueueNameFor(handler.getCommandType()));
            final String routingKey = queueProperties.getCommandBinding(
                handler.getCommandType().getSimpleName(),
                CommandBusImpl.defaultRoutingKeyFor(handler.getCommandType()));
            final Queue queue = new Queue(queueName, true);
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(rabbitBusExchange).with(routingKey));
          });
    }
  }

  public static class QueryQueueRegistrar {

    public QueryQueueRegistrar(final AmqpAdmin amqpAdmin,
                                final TopicExchange rabbitBusExchange,
                                final List<QueryHandler<?, ?>> queryHandlers,
                                final RabbitQueuesProperties queueProperties) {
      queryHandlers.stream()
          .forEach(handler -> {
            final String queueName = queueProperties.getQueryQueueName(
                handler.getQueryType().getSimpleName(),
                QueryBusImpl.defaultQueueNameFor(handler.getQueryType()));
            final String routingKey = queueProperties.getQueryBinding(
                handler.getQueryType().getSimpleName(),
                QueryBusImpl.defaultRoutingKeyFor(handler.getQueryType()));
            final Queue queue = new Queue(queueName, true);
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(rabbitBusExchange).with(routingKey));
          });
    }
  }
}
