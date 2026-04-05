package com.architecture.hexagonal.application.cqrs.query.dispatcher.impl;

import com.architecture.hexagonal.application.cqrs.query.dispatcher.QueryBus;
import com.architecture.hexagonal.application.cqrs.query.handler.QueryHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    if (Objects.isNull(handler)) {
      throw new IllegalStateException("No handler found for query: " + query.getClass());
    }

    return handler.handle(query);
  }
}
