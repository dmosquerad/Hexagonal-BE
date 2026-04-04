package com.architecture.hexagonal.infrastructure.inbound.rabbitmq.query;

import com.architecture.hexagonal.application.bus.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rabbitmq.config.RabbitQueuesProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueryBusImpl implements QueryBus {

  private static final String QUERY_QUEUE_PREFIX = "query-";
  private static final String QUERY_BINDING_PREFIX = "query.";

  private final RabbitTemplate rabbitTemplate;
  private final RabbitQueuesProperties rabbitQueuesProperties;

  public QueryBusImpl(final RabbitTemplate rabbitTemplate,
                      final RabbitQueuesProperties rabbitQueuesProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitQueuesProperties = rabbitQueuesProperties;
  }

  public static String defaultQueueNameFor(final Class<?> queryType) {
    return QUERY_QUEUE_PREFIX + queryType.getSimpleName();
  }

  public static String defaultRoutingKeyFor(final Class<?> queryType) {
    return QUERY_BINDING_PREFIX + queryType.getSimpleName();
  }

  public String queueNameFor(final Class<?> queryType) {
    return rabbitQueuesProperties.getQueryQueueName(
        queryType.getSimpleName(),
        defaultQueueNameFor(queryType));
  }

  public String routingKeyFor(final Class<?> queryType) {
    return rabbitQueuesProperties.getQueryBinding(
        queryType.getSimpleName(),
        defaultRoutingKeyFor(queryType));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Q, R> R execute(final Q query) throws Exception {
    final String routingKey = routingKeyFor(query.getClass());
    final Object reply = rabbitTemplate.convertSendAndReceive(
        rabbitQueuesProperties.getExchange(), routingKey, query);
    if (reply == null) {
      throw new IllegalStateException("No reply received for query: " + query.getClass());
    }
    return (R) reply;
  }
}
