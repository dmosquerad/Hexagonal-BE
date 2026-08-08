package com.architecture.hexagonal.infrastructure.outbound.message.outbox.adapter;

import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming.OutboxNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxProcessAdapterImplTest {

  @InjectMocks private OutboxProcessAdapterImpl outboxProcessAdapterImpl;

  @Mock private OutboxService outboxService;

  @Test
  void process_shouldDelegateToOutboxService_whenAggregateTypeIsUser() {
    final Outbox outboxDo =
        OutboxTestDataBuilder.builder()
            .aggregateType(UserMessageNaming.AGGREGATE_USER_TYPE)
            .build()
            .outbox();

    outboxProcessAdapterImpl.process(outboxDo);

    Mockito.verify(outboxService).process(outboxDo);
  }

  @Test
  void process_shouldThrowIllegalArgumentException_whenAggregateTypeIsUnsupported() {
    final Outbox outboxDo = OutboxTestDataBuilder.builder().aggregateType("").build().outbox();

    AssertionsForClassTypes.assertThatThrownBy(() -> outboxProcessAdapterImpl.process(outboxDo))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_AGGREGATE_TYPE);

    Mockito.verifyNoInteractions(outboxService);
  }
}
