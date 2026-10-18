package com.architecture.hexagonal.infrastructure.bus.middleware;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusLoggingMiddleware implements BusMiddleware {

  @Override
  public <R> R execute(final Object message, final Supplier<R> dispatch) {
    final long startTime = System.nanoTime();
    try {
      final R result = dispatch.get();
      log.info("{} completed in {} ms", message.getClass().getSimpleName(), elapsed(startTime));
      return result;
    } catch (final RuntimeException exception) {
      log.error(
          "{} failed in {} ms ({})",
          message.getClass().getSimpleName(),
          elapsed(startTime),
          exception.getClass().getSimpleName());
      throw exception;
    }
  }

  private long elapsed(final long startTime) {
    return (System.nanoTime() - startTime) / 1_000_000;
  }
}
