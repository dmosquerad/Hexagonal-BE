package com.architecture.hexagonal.infrastructure.inbound.rabbitmq.query;

import com.architecture.hexagonal.application.handler.query.QueryHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueryBusListener {

  private final Map<Class<?>, QueryHandler<?, ?>> handlersByQuery = new HashMap<>();

  public QueryBusListener(final List<QueryHandler<?, ?>> queryHandlers) {
    queryHandlers.forEach(handler -> handlersByQuery.put(handler.getQueryType(), handler));
  }

  @RabbitListener(queues = "#{queryQueueNames}")
  public Object onQuery(final Object query) throws Exception {
    final QueryHandler handler = handlersByQuery.get(query.getClass());
    if (Objects.isNull(handler)) {
      throw new IllegalStateException("No query handler registered for query: " + query.getClass());
    }
    return handler.handle(query);
  }
}
