package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.impl;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming.OutboxNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox.PayloadTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserActionType;
import org.assertj.core.api.AssertionsForClassTypes;
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
class UserOutboxServiceImplTest {

  @InjectMocks private UserOutboxServiceImpl userOutboxServiceImpl;

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @Spy
  private final UserFromOutboxMapper userFromOutboxMapper =
      Mappers.getMapper(UserFromOutboxMapper.class);

  @Test
  void process_shouldPublishCreatedEvent_whenActionIsUserCreated() {
    final PayloadVo payload = PayloadTestDataBuilder.builder().build().payload();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(UserActionType.USER_CREATED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    userOutboxServiceImpl.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserCreated(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserCreated.class));
  }

  @Test
  void process_shouldPublishUpdatedEvent_whenActionIsUserUpdated() {
    final PayloadVo payload = PayloadTestDataBuilder.builder().build().payload();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(UserActionType.USER_UPDATED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    userOutboxServiceImpl.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserUpdated(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserUpdated.class));
  }

  @Test
  void process_shouldPublishDeletedEvent_whenActionIsUserDeleted() {
    final PayloadVo payload = PayloadTestDataBuilder.builder().build().payload();
    final Outbox outbox =
        OutboxTestDataBuilder.builder()
            .action(UserActionType.USER_DELETED.getActionType())
            .payload(payload)
            .build()
            .outbox();

    userOutboxServiceImpl.process(outbox);

    Mockito.verify(userFromOutboxMapper).toUserDeleted(payload);
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserDeleted.class));
  }

  @Test
  void process_shouldThrowIllegalArgumentException_whenActionIsUnsupported() {
    final Outbox outbox = OutboxTestDataBuilder.builder().action("").payload(null).build().outbox();

    AssertionsForClassTypes.assertThatThrownBy(() -> userOutboxServiceImpl.process(outbox))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_USER_ACTION);

    Mockito.verifyNoInteractions(applicationEventPublisher);
  }
}
