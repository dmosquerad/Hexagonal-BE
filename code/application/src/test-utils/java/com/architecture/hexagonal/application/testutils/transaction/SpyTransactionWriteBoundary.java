package com.architecture.hexagonal.application.testutils.transaction;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;

import java.util.function.Supplier;

public final class SpyTransactionWriteBoundary
    implements TransactionWriteBoundary {

  @Override
  public <T> T execute(final Supplier<T> action) {
    return action.get();
  }

  @Override
  public void executeWithoutResult(final Runnable action) {
    action.run();
  }
}
