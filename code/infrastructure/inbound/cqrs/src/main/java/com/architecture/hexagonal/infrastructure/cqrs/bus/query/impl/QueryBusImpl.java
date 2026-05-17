package com.architecture.hexagonal.infrastructure.cqrs.bus.query.impl;

import com.architecture.hexagonal.domain.exception.DomainException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.architecture.hexagonal.application.common.cqrs.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.cqrs.bus.query.QueryBus;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class QueryBusImpl implements QueryBus {

  private final Map<Class<?>, QueryHandler<?, ?>> handlersByQuery;

  public QueryBusImpl(final List<QueryHandler<?, ?>> queryHandlers) {
    this.handlersByQuery = queryHandlers.stream()
        .collect(Collectors.toMap(
            handler -> ResolvableType.forClass(handler.getClass())
                .as(QueryHandler.class)
                .getGeneric(0)
                .resolve(),
            handler -> handler
        ));
  }

  @Override
  public <Q, R> R execute(final Q query) throws DomainException {
    final QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlersByQuery.get(query.getClass());

    if (Objects.isNull(handler)) {
      throw new IllegalStateException("No handler found for query: " + query.getClass());
    }

    return handler.handle(query);
  }
}
