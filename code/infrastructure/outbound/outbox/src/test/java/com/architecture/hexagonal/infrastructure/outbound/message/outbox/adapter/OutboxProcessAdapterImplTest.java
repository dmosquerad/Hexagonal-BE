package com.architecture.hexagonal.infrastructure.outbound.message.outbox.adapter;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming.OutboxNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import java.util.Map;
import java.util.function.Consumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxProcessAdapterImplTest {

  @Spy @InjectMocks private OutboxProcessAdapterImpl outboxProcessAdapterImpl;

  @Mock private OutboxService outboxService;

  @Test
  void process_shouldDelegateToOutboxService_whenAggregateTypeIsUser() {
    final Outbox outbox =
        OutboxTestDataBuilder.builder().aggregateType(AggregateTypeVo.USER).build().outbox();

    outboxProcessAdapterImpl.process(outbox);

    Mockito.verify(outboxService).process(outbox);
  }

  @Test
  void shouldHaveHandlerForEveryAggregateType() {
    Assertions.assertThatCode(outboxProcessAdapterImpl::validateAggregateHandlers)
        .doesNotThrowAnyException();
  }

  @Test
  void shouldThrowWhenAggregateHandlerIsMissing() {
    final Map<AggregateTypeVo, Consumer<Outbox>> handlers = Map.of();

    Mockito.doReturn(Map.of()).when(outboxProcessAdapterImpl).aggregateHandlers();

    Assertions.assertThatThrownBy(() -> outboxProcessAdapterImpl.validateAggregateHandlers())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_AGGREGATE_TYPE);
  }
}
