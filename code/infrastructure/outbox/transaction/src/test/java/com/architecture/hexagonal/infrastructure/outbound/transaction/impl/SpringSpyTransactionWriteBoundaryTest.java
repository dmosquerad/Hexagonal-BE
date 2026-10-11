package com.architecture.hexagonal.infrastructure.outbound.transaction.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
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
class SpringSpyTransactionWriteBoundaryTest {

  @Mock private PlatformTransactionManager platformTransactionManager;

  @Mock private TransactionStatus transactionStatus;

  @Test
  void executeShouldReturnResultInWritableTransaction() {
    Mockito.when(platformTransactionManager.getTransaction(Mockito.any()))
        .thenReturn(transactionStatus);
    final SpringTransactionWriteBoundary transactionBoundary =
        new SpringTransactionWriteBoundary(platformTransactionManager);

    final String action = "result";

    final String result = transactionBoundary.execute(() -> action);

    final ArgumentCaptor<TransactionDefinition> definitionCaptor =
        ArgumentCaptor.forClass(TransactionDefinition.class);
    Mockito.verify(platformTransactionManager).getTransaction(definitionCaptor.capture());
    Mockito.verify(platformTransactionManager).commit(transactionStatus);
    assertThat(result).isEqualTo(action);
    assertThat(definitionCaptor.getValue().isReadOnly()).isFalse();
  }

  @Test
  void executeWithoutResultShouldUseWritableTransaction() {
    Mockito.when(platformTransactionManager.getTransaction(Mockito.any()))
        .thenReturn(transactionStatus);
    final SpringTransactionWriteBoundary transactionBoundary =
        new SpringTransactionWriteBoundary(platformTransactionManager);
    final AtomicInteger actionCalls = new AtomicInteger();
    final Runnable action = actionCalls::incrementAndGet;

    transactionBoundary.executeWithoutResult(action);

    final ArgumentCaptor<TransactionDefinition> definitionCaptor =
        ArgumentCaptor.forClass(TransactionDefinition.class);
    Mockito.verify(platformTransactionManager).getTransaction(definitionCaptor.capture());
    Mockito.verify(platformTransactionManager).commit(transactionStatus);
    assertThat(actionCalls).hasValue(1);
    assertThat(definitionCaptor.getValue().isReadOnly()).isFalse();
  }
}
