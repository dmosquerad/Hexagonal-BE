package com.architecture.hexagonal.domain.port.in.transaction;

import java.util.function.Supplier;

public interface TransactionReadBoundary {

  <T> T execute(Supplier<T> action);
}
