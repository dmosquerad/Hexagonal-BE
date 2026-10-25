package com.architecture.hexagonal.infrastructure.bus.middleware.impl;

import com.architecture.hexagonal.infrastructure.bus.middleware.BusMiddleware;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusLoggingMiddleware implements BusMiddleware {

  private final Clock clock;

  @Override
  public <R> R execute(final Object message, final Supplier<R> dispatch) {
    final Instant startTime = clock.instant();
    try {
      final R result = dispatch.get();
      log.info(
          "{} completed in {} ms",
          message.getClass().getSimpleName(),
          Duration.between(startTime, clock.instant()).toMillis());
      return result;
    } catch (final RuntimeException exception) {
      log.error(
          "{} failed in {} ms ({})",
          message.getClass().getSimpleName(),
          Duration.between(startTime, clock.instant()).toMillis(),
          exception.getClass().getSimpleName());
      throw exception;
    }
  }
}
