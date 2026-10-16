package com.architecture.hexagonal.infrastructure.bus.middleware;

import java.util.function.Supplier;

public interface BusMiddleware {

  <R> R execute(Object message, Supplier<R> dispatch);
}
