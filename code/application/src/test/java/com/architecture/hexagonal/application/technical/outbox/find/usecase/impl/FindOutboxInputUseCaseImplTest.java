package com.architecture.hexagonal.application.technical.outbox.find.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.application.testutils.outbox.find.FindOutboxInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.outbox.find.OutboxStatusAndAggregateProjectorTestDataBuilder;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase.impl.FindOutboxUseCaseImpl;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindOutboxInputUseCaseImplTest {

  @InjectMocks private FindOutboxUseCaseImpl findPendingOutboxEventsUseCaseImpl;

  @Mock private OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Test
  void executeShouldReturnPendingEvents() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector =
        OutboxStatusAndAggregateProjectorTestDataBuilder.builder()
            .build()
            .outboxStatusAndAggregateProjector();

    final FindOutboxInput findOutboxInput =
        FindOutboxInputTestDataBuilder.builder().build().findPendingOutboxEventsInput();
    Mockito.when(
            outboxRepositoryReadPort.findByStatusAndAggregateType(
                outboxStatusAndAggregateProjector))
        .thenReturn(List.of(outbox));

    final List<Outbox> result = findPendingOutboxEventsUseCaseImpl.execute(findOutboxInput);

    AssertionsForInterfaceTypes.assertThat(result).containsExactly(outbox);
    Mockito.verify(outboxRepositoryReadPort)
        .findByStatusAndAggregateType(outboxStatusAndAggregateProjector);
  }
}
