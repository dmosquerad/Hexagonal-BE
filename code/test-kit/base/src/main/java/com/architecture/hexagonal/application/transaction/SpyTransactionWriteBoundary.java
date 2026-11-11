package com.architecture.hexagonal.application.transaction;

import com.architecture.hexagonal.domain.port.in.transaction.TransactionWriteBoundary;
import java.util.function.Supplier;

public final class SpyTransactionWriteBoundary implements TransactionWriteBoundary {

  @Override
  public <T> T execute(final Supplier<T> action) {
    return action.get();
  }

  @Override
  public void executeWithoutResult(final Runnable action) {
    action.run();
  }
}
