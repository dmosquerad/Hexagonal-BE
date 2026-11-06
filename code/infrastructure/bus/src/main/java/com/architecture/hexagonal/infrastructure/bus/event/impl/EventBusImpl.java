package com.architecture.hexagonal.infrastructure.bus.event.impl;

import com.architecture.hexagonal.application.port.in.bus.event.EventHandler;
import com.architecture.hexagonal.application.port.out.bus.event.EventBus;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.middleware.BusMiddleware;
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
          final List<EventHandler> handlers = handlerFinder.findAll(event, EventHandler.class);
          for (final EventHandler<E> handler : handlers) {
            try {
              handler.handle(event);
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
