package com.architecture.hexagonal.infrastructure.inbound.orchestration.config;

import java.util.function.Supplier;

public interface TransactionBoundary {

  <T> T read(Supplier<T> action);

  <T> T write(Supplier<T> action);
}
