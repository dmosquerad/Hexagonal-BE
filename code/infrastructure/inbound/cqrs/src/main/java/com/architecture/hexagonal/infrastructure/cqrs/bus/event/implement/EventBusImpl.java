package com.architecture.hexagonal.infrastructure.cqrs.bus.event.implement;

import com.architecture.hexagonal.application.common.cqrs.event.EventHandler;
import com.architecture.hexagonal.domain.exception.DomainException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.architecture.hexagonal.infrastructure.cqrs.bus.event.EventBus;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class EventBusImpl implements EventBus {

  private final Map<Class<?>, List<EventHandler<?>>> handlersByEvent;

  public EventBusImpl(final List<EventHandler<?>> eventHandlers) {
    this.handlersByEvent = eventHandlers.stream()
        .collect(Collectors.groupingBy(
            handler -> ResolvableType.forClass(handler.getClass())
                .as(EventHandler.class)
                .getGeneric(0)
                .resolve()
        ));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <E> void publish(final E event) throws DomainException {
    final List<EventHandler<E>> handlers =
        (List<EventHandler<E>>) (List<?>) handlersByEvent.getOrDefault(
            event.getClass(), Collections.emptyList()
        );

    for (final EventHandler<E> handler : handlers) {
      handler.handle(event);
    }
  }
}
