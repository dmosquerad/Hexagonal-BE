package com.architecture.hexagonal.infrastructure.transaction.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class SpringTransactionReadBoundary implements TransactionReadBoundary {

  private final TransactionTemplate readTransactionTemplate;

  public SpringTransactionReadBoundary(
      final PlatformTransactionManager platformTransactionManager) {
    final TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
    template.setReadOnly(true);

    this.readTransactionTemplate = template;
  }

  @Override
  public <T> T execute(final Supplier<T> action) {
    return readTransactionTemplate.execute(status -> action.get());
  }
}
