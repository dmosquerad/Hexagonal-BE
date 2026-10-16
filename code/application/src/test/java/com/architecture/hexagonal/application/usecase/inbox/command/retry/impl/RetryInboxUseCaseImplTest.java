package com.architecture.hexagonal.application.usecase.inbox.command.retry.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.inbox.InboxService;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.bus.model.retry.command.RetryInboxeventCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
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
class RetryInboxUseCaseImplTest {

  @Mock private InboxService inboxService;

  @Spy private TransactionReadBoundary transactionReadBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private RetryInboxUseCaseImpl retryInboxUseCaseImpl;

  @Test
  void handleShouldProcessPendingEventsForUserAggregateType() {
    final Inbox inbox = InboxTestDataBuilder.builder().build().inbox();
    Mockito.when(inboxService.findPending(Mockito.any(AggregateTypeVo.class)))
        .thenReturn(List.of(inbox));

    final Void result = retryInboxUseCaseImpl.handle(new RetryInboxeventCommand());

    AssertionsForClassTypes.assertThat(result).isNull();

    Mockito.verify(inboxService).findPending(AggregateTypeVo.USER);
    Mockito.verify(transactionReadBoundary).execute(Mockito.any());
    Mockito.verify(inboxService).process(inbox);
  }
}
