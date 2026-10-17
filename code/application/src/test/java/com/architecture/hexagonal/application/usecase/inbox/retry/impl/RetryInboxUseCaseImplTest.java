package com.architecture.hexagonal.application.usecase.inbox.retry.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.application.usecase.generated.retry.RetryInboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetryInboxUseCaseImplTest {

  @Mock private InboxService inboxService;

  @Spy private TransactionReadBoundary transactionReadBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private RetryInboxUseCaseImpl useCase;

  @Test
  void handleShouldProcessPendingEventsForEachAggregateType() {
    final Inbox inbox = Mockito.mock(Inbox.class);
    Mockito.when(inboxService.findPending(Mockito.any(AggregateTypeVo.class)))
        .thenReturn(List.of(inbox));

    useCase.handle(new RetryInboxeventCommand());

    for (final AggregateTypeVo aggregateType : AggregateTypeVo.values()) {
      Mockito.verify(inboxService).findPending(aggregateType);
    }
    Mockito.verify(inboxService, Mockito.times(AggregateTypeVo.values().length)).process(inbox);
    Mockito.verify(transactionReadBoundary, Mockito.times(AggregateTypeVo.values().length))
        .execute(Mockito.any());
  }
}
