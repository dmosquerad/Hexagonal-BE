package com.architecture.hexagonal.application.bus.query.impl;

import com.architecture.hexagonal.application.bus.query.QueryBus;
import com.architecture.hexagonal.application.handler.query.QueryHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class QueryBusImpl implements QueryBus {

  private final List<QueryHandler<?, ?>> queryHandlers;

  private final Map<Class<?>, QueryHandler<?, ?>> handlersByQuery = new HashMap<>();

  public QueryBusImpl(final List<QueryHandler<?, ?>> queryHandlers) {
    this.queryHandlers = queryHandlers;
    this.queryHandlers.forEach(handler -> handlersByQuery.put(handler.getQueryType(), handler));
  }

  @Override
  public <Q, R> R execute(final Q query) throws Exception {
    final QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlersByQuery.get(query.getClass());

    if (handler == null) {
      throw new IllegalStateException("No handler found for query: " + query.getClass());
    }

    return handler.handle(query);
  }
}
