package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.transaction;

import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import java.util.function.Supplier;

public class TransactionBoundaryTest implements TransactionBoundary {

  @Override
  public <T> T read(Supplier<T> action) {
    return action.get();
  }

  @Override
  public <T> T write(Supplier<T> action) {
    return action.get();
  }
}
