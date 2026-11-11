package com.architecture.hexagonal.domain.port.in.transaction;

import java.util.function.Supplier;

public interface TransactionWriteBoundary {

  <T> T execute(Supplier<T> action);

  void executeWithoutResult(Runnable action);
}
