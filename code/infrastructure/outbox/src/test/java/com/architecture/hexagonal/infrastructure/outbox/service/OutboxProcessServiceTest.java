package com.architecture.hexagonal.infrastructure.outbox.service;

import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserData;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbox.naming.OutboxNaming;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class OutboxProcessServiceTest {

  @Spy @InjectMocks private OutboxProcessService outboxProcessService;

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @Spy
  private final UserFromOutboxMapper userFromOutboxMapper =
      Mappers.getMapper(UserFromOutboxMapper.class);

  @Test
  void processShouldPublishCreatedEvent() {
    final PayloadVo payload = PayloadVoTestDataBuilder.<UserData>builder().build().payloadVo();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    outboxProcessService.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserCreated(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserCreated.class));
  }

  @Test
  void processShouldPublishUpdatedEvent() {
    final PayloadVo payload = PayloadVoTestDataBuilder.<UserData>builder().build().payloadVo();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    outboxProcessService.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserUpdated(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserUpdated.class));
  }

  @Test
  void processShouldPublishDeletedEvent() {
    final PayloadVo payload = PayloadVoTestDataBuilder.<UserData>builder().build().payloadVo();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .aggregateType(AggregateTypeVo.USER)
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    outboxProcessService.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserDeleted(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserDeleted.class));
  }

  @Test
  void shouldHaveHandlerForEveryAggregateType() {
    Assertions.assertThatCode(outboxProcessService::validateAggregateHandlers)
        .doesNotThrowAnyException();
  }

  @Test
  void shouldThrowWhenAggregateHandlerIsMissing() {
    Mockito.when(outboxProcessService.aggregateHandlers()).thenReturn(Map.of());

    Assertions.assertThatThrownBy(outboxProcessService::validateAggregateHandlers)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_AGGREGATE_TYPE);
  }

  @Test
  void processShouldThrowWhenActionIsUnsupported() {
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .aggregateType(AggregateTypeVo.USER)
            .action("")
            .payload(null)
            .build()
            .outbox();

    Assertions.assertThatThrownBy(() -> outboxProcessService.process(outbox))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_USER_ACTION);

    Mockito.verifyNoInteractions(applicationEventPublisher);
  }
}
