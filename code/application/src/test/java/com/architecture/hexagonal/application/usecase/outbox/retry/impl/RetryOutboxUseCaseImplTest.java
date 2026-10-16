package com.architecture.hexagonal.application.usecase.outbox.retry.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.service.technical.outbox.OutboxService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionWriteBoundary;
import com.architecture.hexagonal.contract.application.usecase.generated.retry.RetryOutboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetryOutboxUseCaseImplTest {

  @Mock private OutboxService outboxService;

  @Spy private TransactionReadBoundary transactionReadBoundary = new SpyTransactionReadBoundary();

  @Spy
  private TransactionWriteBoundary transactionWriteBoundary = new SpyTransactionWriteBoundary();

  @InjectMocks private RetryOutboxUseCaseImpl useCase;

  @Test
  void handleShouldProcessPendingEventsForEachAggregateType() {
    final int aggregateTypes = AggregateTypeVo.values().length;
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    Mockito.when(outboxService.find(Mockito.any(OutboxStatusAndAggregateProjector.class)))
        .thenReturn(List.of(outbox));

    final Void result = useCase.handle(new RetryOutboxeventCommand());

    AssertionsForClassTypes.assertThat(result).isNull();
    Mockito.verify(outboxService, Mockito.times(aggregateTypes))
        .find(Mockito.any(OutboxStatusAndAggregateProjector.class));
    Mockito.verify(transactionReadBoundary, Mockito.times(aggregateTypes)).execute(Mockito.any());
    Mockito.verify(outboxService, Mockito.times(aggregateTypes)).process(outbox);
    Mockito.verify(transactionWriteBoundary, Mockito.times(aggregateTypes))
        .executeWithoutResult(Mockito.any(Runnable.class));
  }
}
