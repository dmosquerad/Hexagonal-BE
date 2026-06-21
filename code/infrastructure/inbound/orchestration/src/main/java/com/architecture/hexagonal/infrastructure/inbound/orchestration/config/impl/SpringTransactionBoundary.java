package com.architecture.hexagonal.infrastructure.inbound.orchestration.config.impl;

import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class SpringTransactionBoundary implements TransactionBoundary {

  private final TransactionTemplate readTransactionTemplate;
  private final TransactionTemplate writeTransactionTemplate;

  public SpringTransactionBoundary(PlatformTransactionManager platformTransactionManager) {
    this.readTransactionTemplate = createReadTemplate(platformTransactionManager);
    this.writeTransactionTemplate = createWriteTemplate(platformTransactionManager);
  }

  private TransactionTemplate createReadTemplate(
      PlatformTransactionManager platformTransactionManager) {
    TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
    template.setReadOnly(true);
    return template;
  }

  private TransactionTemplate createWriteTemplate(
      PlatformTransactionManager platformTransactionManager) {
    return new TransactionTemplate(platformTransactionManager);
  }

  @Override
  public <T> T read(Supplier<T> action) {
    return readTransactionTemplate.execute(status -> action.get());
  }

  @Override
  public <T> T write(Supplier<T> action) {
    return writeTransactionTemplate.execute(status -> action.get());
  }
}
