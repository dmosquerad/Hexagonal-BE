package com.architecture.hexagonal.application.port.out.transaction;

import java.util.function.Supplier;

public interface TransactionReadBoundary {

  <T> T execute(Supplier<T> action);
}
