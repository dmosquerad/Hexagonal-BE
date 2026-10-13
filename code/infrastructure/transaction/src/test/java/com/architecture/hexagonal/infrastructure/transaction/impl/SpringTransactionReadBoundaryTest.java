package com.architecture.hexagonal.infrastructure.transaction.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@ExtendWith(MockitoExtension.class)
class SpringTransactionReadBoundaryTest {

  @Mock private PlatformTransactionManager platformTransactionManager;

  @Mock private TransactionStatus transactionStatus;

  @Test
  void executeShouldUseReadOnlyTransaction() {
    Mockito.when(platformTransactionManager.getTransaction(Mockito.any()))
        .thenReturn(transactionStatus);

    final SpringTransactionReadBoundary transactionBoundary =
        new SpringTransactionReadBoundary(platformTransactionManager);

    final String action = "result";

    final String result = transactionBoundary.execute(() -> action);

    final ArgumentCaptor<TransactionDefinition> definitionCaptor =
        ArgumentCaptor.forClass(TransactionDefinition.class);
    Mockito.verify(platformTransactionManager).getTransaction(definitionCaptor.capture());
    Mockito.verify(platformTransactionManager).commit(transactionStatus);
    Assertions.assertThat(result).isEqualTo(action);
    Assertions.assertThat(definitionCaptor.getValue().isReadOnly()).isTrue();
  }
}
