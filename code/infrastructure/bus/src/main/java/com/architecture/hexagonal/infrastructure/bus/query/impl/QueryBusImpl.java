package com.architecture.hexagonal.infrastructure.bus.query.impl;

import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.bus.MessageHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.query.QueryBus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryBusImpl implements QueryBus {

  private final MessageHandlerFinder messageHandlerFinder;

  @Override
  public <Q, R> R execute(final @NonNull Q query) {
    final QueryHandler<Q, R> handler = messageHandlerFinder.find(query, QueryHandler.class);
    return handler.handle(query);
  }
}
