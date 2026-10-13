package com.architecture.hexagonal.application.port.out.transaction;

import java.util.function.Supplier;

public interface TransactionWriteBoundary {

  <T> T execute(Supplier<T> action);

  void executeWithoutResult(Runnable action);
}
