package com.architecture.hexagonal.infrastructure.bus.query.impl;

import com.architecture.hexagonal.domain.port.in.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryBus;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.BusMessageValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryBusImpl implements QueryBus {

  private final BusHandlerFinder handlerFinder;
  private final BusMessageValidator messageValidator;
  private final BusMiddleware loggingMiddleware;

  @Override
  public <Q, R> R execute(final @NonNull Q query) {
    return loggingMiddleware.execute(
        query,
        () -> {
          messageValidator.validate(query);
          final QueryHandler<Q, R> handler = handlerFinder.find(query, QueryHandler.class);
          return handler.handle(query);
        });
  }
}
