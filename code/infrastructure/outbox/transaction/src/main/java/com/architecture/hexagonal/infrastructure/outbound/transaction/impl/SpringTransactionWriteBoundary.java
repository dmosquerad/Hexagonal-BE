package com.architecture.hexagonal.infrastructure.outbound.transaction.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class SpringTransactionWriteBoundary implements TransactionWriteBoundary {

  private final TransactionTemplate writeTransactionTemplate;

  public SpringTransactionWriteBoundary(
      final PlatformTransactionManager platformTransactionManager) {
    this.writeTransactionTemplate = new TransactionTemplate(platformTransactionManager);
  }

  @Override
  public <T> T execute(final Supplier<T> action) {
    return writeTransactionTemplate.execute(status -> action.get());
  }

  @Override
  public void executeWithoutResult(final Runnable action) {
    writeTransactionTemplate.executeWithoutResult(status -> action.run());
  }
}
