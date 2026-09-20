package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.outbox.retry;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.FindOutboxUseCase;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.input.ProcessOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.process.usecase.ProcessOutboxUseCase;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.retry.RetryOutboxeventCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.FindPendingOutboxEventsInputTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.OutboxTestDataBuilder;
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

  @MockitoBean private FindOutboxUseCase findOutboxUseCase;

  @MockitoBean private ProcessOutboxUseCase processOutboxUseCase;

  @Test
  void handleShouldProcessPendingEventsWhenAggregateIdsAreUnique() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final FindOutboxInput findOutboxInput = FindPendingOutboxEventsInputTestDataBuilder.builder().build().findPendingOutboxEventsInput();
    Mockito.when(findOutboxUseCase.execute(findOutboxInput)).thenReturn(List.of(outbox));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findOutboxUseCase).execute(findOutboxInput);
    Mockito.verify(processOutboxUseCase).execute(ArgumentMatchers.any(ProcessOutboxInput.class));
  }

  @Test
  void handleShouldBlockDuplicatePendingEventWhenAggregateIdIsRepeated() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outbox();
    final Outbox duplicateOutbox =
        OutboxTestDataBuilder.builder().build().outbox();
    final FindOutboxInput findOutboxInput = FindPendingOutboxEventsInputTestDataBuilder.builder().build().findPendingOutboxEventsInput();

    Mockito.when(findOutboxUseCase.execute(findOutboxInput))
        .thenReturn(List.of(outbox, duplicateOutbox));

    retryOutboxEventsCommandHandlerImpl.handle(new RetryOutboxeventCommandDto());

    Mockito.verify(findOutboxUseCase).execute(findOutboxInput);
    Mockito.verify(processOutboxUseCase, Mockito.times(2)).execute(ArgumentMatchers.any(ProcessOutboxInput.class));
  }
}
