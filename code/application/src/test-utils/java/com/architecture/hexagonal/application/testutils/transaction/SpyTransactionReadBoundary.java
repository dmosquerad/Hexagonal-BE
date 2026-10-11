package com.architecture.hexagonal.application.testutils.transaction;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;

import java.util.function.Supplier;

public final class SpyTransactionReadBoundary
    implements TransactionReadBoundary {

  @Override
  public <T> T execute(final Supplier<T> action) {
    return action.get();
  }

}
