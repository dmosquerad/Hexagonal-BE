package com.architecture.hexagonal.application.technical.outbox.find.usecase.impl;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.entity.OutboxDoTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.OutboxDo;
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
    final OutboxDo outboxDo = OutboxDoTestDataBuilder.builder().build().outboxDo();
    Mockito.when(outboxRepositoryReadPort.findPendingEvents()).thenReturn(List.of(outboxDo));

    final List<OutboxDo> result = findPendingOutboxEventsUseCaseImpl.execute();

    AssertionsForInterfaceTypes.assertThat(result).containsExactly(outboxDo);
    Mockito.verify(outboxRepositoryReadPort).findPendingEvents();
  }
}
