package com.architecture.hexagonal.infrastructure.bus.middleware.impl;

import com.architecture.hexagonal.domain.port.in.bus.middleware.BusMiddleware;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusLoggingMiddleware implements BusMiddleware {

  @Override
  public <R> R execute(final Object message, final Supplier<R> dispatch) {
    final Instant startTime = Instant.now();
    try {
      final R result = dispatch.get();
      log.info(
          "{} completed in {} ms",
          message.getClass().getSimpleName(),
          Duration.between(startTime, Instant.now()).toMillis());
      return result;
    } catch (final RuntimeException exception) {
      log.error(
          "{} failed in {} ms ({})",
          message.getClass().getSimpleName(),
          Duration.between(startTime, Instant.now()).toMillis(),
          exception.getClass().getSimpleName());
      throw exception;
    }
  }
}
