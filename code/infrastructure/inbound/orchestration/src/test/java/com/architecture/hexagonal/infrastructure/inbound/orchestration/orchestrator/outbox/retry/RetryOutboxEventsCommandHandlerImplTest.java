package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.outbox.retry;

import com.architecture.hexagonal.application.technical.outbox.block.usecase.BlockOutboxEventUseCase;
import com.architecture.hexagonal.application.technical.outbox.find.usecase.FindPendingOutboxEventsUseCase;
import com.architecture.hexagonal.application.technical.outbox.process.usecase.ProcessOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.OutboxEventDoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.transaction.TransactionBoundaryTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetryOutboxEventsCommandHandlerImplTest {

  @InjectMocks private RetryOutboxEventsCommandHandlerImpl retryOutboxEventsCommandHandlerImpl;

  @Mock private FindPendingOutboxEventsUseCase findPendingOutboxEventsUseCase;

  @Mock private BlockOutboxEventUseCase blockOutboxEventUseCase;

  @Mock private ProcessOutboxEventUseCase processOutboxEventUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handleShouldProcessPendingEventsWhenAggregateIdsAreUnique() {
    final OutboxDo outboxDo = OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();
    Mockito.when(findPendingOutboxEventsUseCase.execute()).thenReturn(List.of(outboxDo));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findPendingOutboxEventsUseCase).execute();
    Mockito.verify(transactionBoundary).read(ArgumentMatchers.any());
    Mockito.verify(transactionBoundary).write(ArgumentMatchers.any());
    Mockito.verify(processOutboxEventUseCase).execute(outboxDo);
    Mockito.verifyNoInteractions(blockOutboxEventUseCase);
  }

  @Test
  void handleShouldBlockDuplicatePendingEventWhenAggregateIdIsRepeated() {
    final OutboxDo outboxDo = OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();
    final OutboxDo duplicateOutboxDo =
        OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();

    Mockito.when(findPendingOutboxEventsUseCase.execute())
        .thenReturn(List.of(outboxDo, duplicateOutboxDo));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findPendingOutboxEventsUseCase).execute();
    Mockito.verify(transactionBoundary).read(ArgumentMatchers.any());
    Mockito.verify(transactionBoundary, Mockito.times(2)).write(ArgumentMatchers.any());
    Mockito.verify(processOutboxEventUseCase).execute(outboxDo);
    Mockito.verify(blockOutboxEventUseCase).execute(duplicateOutboxDo);
  }
}
