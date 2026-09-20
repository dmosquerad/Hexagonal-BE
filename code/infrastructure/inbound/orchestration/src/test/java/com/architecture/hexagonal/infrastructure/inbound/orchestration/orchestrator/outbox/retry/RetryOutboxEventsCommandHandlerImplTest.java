package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.outbox.retry;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.FindOutboxUseCase;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.ProcessOutboxUseCase;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.FindPendingOutboxEventsInputTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.OutboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.ProcessOutboxInputTestDataBuilder;
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

  @Mock private FindOutboxUseCase findOutboxUseCase;

  @Mock private ProcessOutboxUseCase processOutboxUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handleShouldProcessPendingEventsWhenAggregateIdsAreUnique() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final FindOutboxInput findOutboxInput =
        FindPendingOutboxEventsInputTestDataBuilder.builder()
            .build()
            .findPendingOutboxEventsInput();
    Mockito.when(findOutboxUseCase.execute(findOutboxInput)).thenReturn(List.of(outbox));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findOutboxUseCase).execute(findOutboxInput);
    Mockito.verify(transactionBoundary).read(ArgumentMatchers.any());
    Mockito.verify(transactionBoundary).write(ArgumentMatchers.any());
    Mockito.verify(processOutboxUseCase).execute(processOutboxInput);
  }

  @Test
  void handleShouldBlockDuplicatePendingEventWhenAggregateIdIsRepeated() {
    final ProcessOutboxInput processOutboxInput =
        ProcessOutboxInputTestDataBuilder.builder().build().processOutboxInput();
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox duplicateOutbox = OutboxTestDataBuilder.builder().build().outbox();
    final FindOutboxInput findOutboxInput =
        FindPendingOutboxEventsInputTestDataBuilder.builder()
            .build()
            .findPendingOutboxEventsInput();

    Mockito.when(findOutboxUseCase.execute(findOutboxInput))
        .thenReturn(List.of(outbox, duplicateOutbox));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findOutboxUseCase).execute(findOutboxInput);
    Mockito.verify(transactionBoundary).read(ArgumentMatchers.any());
    Mockito.verify(transactionBoundary, Mockito.times(2)).write(ArgumentMatchers.any());
    Mockito.verify(processOutboxUseCase, Mockito.times(2)).execute(processOutboxInput);
  }
}
