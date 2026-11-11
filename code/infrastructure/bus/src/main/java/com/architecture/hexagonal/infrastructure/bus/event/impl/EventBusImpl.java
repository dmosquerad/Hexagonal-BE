package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.domain.port.in.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.domain.port.out.bus.event.EventBus;
import com.architecture.hexagonal.domain.port.out.bus.event.EventPublisher;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventBusImpl implements EventBus {

  private final BusHandlerFinder handlerFinder;
  private final BusMiddleware loggingMiddleware;

  @Override
  public <E> void publish(final @NonNull E event) {
    loggingMiddleware.execute(
        event,
        () -> {
          final List<EventPublisher> handlers = handlerFinder.findAll(event, EventPublisher.class);
          for (final EventPublisher<E> handler : handlers) {
            try {
              handler.publish(event);
            } catch (final RuntimeException exception) {
              log.error(
                  "{} failed handling {} ({})",
                  handler.getClass().getSimpleName(),
                  event.getClass().getSimpleName(),
                  exception.getClass().getSimpleName());
            }
          }
          return null;
        });
  }
}
