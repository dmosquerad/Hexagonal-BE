package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.outbox.retry;

import com.architecture.hexagonal.application.technical.outbox.block.usecase.BlockOutboxEventUseCase;
import com.architecture.hexagonal.application.technical.outbox.find.usecase.FindPendingOutboxEventsUseCase;
import com.architecture.hexagonal.application.technical.outbox.process.usecase.ProcessOutboxEventUseCase;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.OutboxEventDoTestDataBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = RetryOutboxEventsCommandHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class RetryOutboxEventsCommandHandlerImplTestIT {

  @Autowired private RetryOutboxEventsCommandHandlerImpl retryOutboxEventsCommandHandlerImpl;

  @MockitoBean private FindPendingOutboxEventsUseCase findPendingOutboxEventsUseCase;

  @MockitoBean private BlockOutboxEventUseCase blockOutboxEventUseCase;

  @MockitoBean private ProcessOutboxEventUseCase processOutboxEventUseCase;

  @Test
  void handleShouldProcessPendingEventsWhenAggregateIdsAreUnique() {
    final Outbox outbox = OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();
    Mockito.when(findPendingOutboxEventsUseCase.execute()).thenReturn(List.of(outbox));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findPendingOutboxEventsUseCase).execute();
    Mockito.verify(processOutboxEventUseCase).execute(ArgumentMatchers.any(Outbox.class));
    Mockito.verifyNoInteractions(blockOutboxEventUseCase);
  }

  @Test
  void handleShouldBlockDuplicatePendingEventWhenAggregateIdIsRepeated() {
    final Outbox outbox = OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();
    final Outbox duplicateOutbox =
        OutboxEventDoTestDataBuilder.builder().build().outboxEventDo();

    Mockito.when(findPendingOutboxEventsUseCase.execute())
        .thenReturn(List.of(outbox, duplicateOutbox));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findPendingOutboxEventsUseCase).execute();
    Mockito.verify(processOutboxEventUseCase).execute(ArgumentMatchers.any(Outbox.class));
    Mockito.verify(blockOutboxEventUseCase).execute(ArgumentMatchers.any(Outbox.class));
  }
}
