package com.architecture.hexagonal.application.technical.outbox.find.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindPendingOutboxEventsUseCaseImplTest {

  @InjectMocks private FindPendingOutboxEventsUseCaseImpl findPendingOutboxEventsUseCaseImpl;

  @Mock private OutboxRepositoryReadPort outboxRepositoryReadPort;

  @Test
  void executeShouldReturnPendingEvents() {
    final Outbox outbox = OutboxTestDataBuilder.builder().build().outboxDo();
    Mockito.when(outboxRepositoryReadPort.findPendingEvents()).thenReturn(List.of(outbox));

    final List<Outbox> result = findPendingOutboxEventsUseCaseImpl.execute();

    AssertionsForInterfaceTypes.assertThat(result).containsExactly(outbox);
    Mockito.verify(outboxRepositoryReadPort).findPendingEvents();
  }
}
