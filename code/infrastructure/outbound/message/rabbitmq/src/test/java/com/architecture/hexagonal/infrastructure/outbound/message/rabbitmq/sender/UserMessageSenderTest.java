package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.rabbitmq.IUserEventsProducer;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserMessageSenderTest {

  @InjectMocks UserMessageSender userMessageSender;

  @Mock IUserEventsProducer userEventStreamPublisher;
  @Mock OutboxRepositoryWritePort outboxRepositoryWritePort;
  @Spy private PayloadMapper payloadMapper = Mappers.getMapper(PayloadMapper.class);

  @Test
  void sendUserCreatedMessageShouldSendToStreamBridgeWhenEventIsReceived() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(userEventStreamPublisher.publishUserCreated(event)).thenReturn(true);

    userMessageSender.sendUserCreatedMessage(event);

    Mockito.verify(userEventStreamPublisher).publishUserCreated(event);
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void sendUserCreatedMessageShouldSaveOutboxEventWhenStreamBridgeFails() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(userEventStreamPublisher.publishUserCreated(event)).thenReturn(false);

    userMessageSender.sendUserCreatedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserCreatedMessageShouldSaveOutboxEventWhenStreamBridgeThrows() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(userEventStreamPublisher.publishUserCreated(event))
        .thenThrow(new IllegalStateException("broker unavailable"));

    userMessageSender.sendUserCreatedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserUpdatedMessageShouldSendToStreamBridgeWhenEventIsReceived() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    Mockito.when(userEventStreamPublisher.publishUserUpdated(event)).thenReturn(true);

    userMessageSender.sendUserUpdatedMessage(event);

    Mockito.verify(userEventStreamPublisher).publishUserUpdated(event);
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void sendUserUpdatedMessageShouldSaveOutboxEventWhenStreamBridgeFails() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    Mockito.when(userEventStreamPublisher.publishUserUpdated(event)).thenReturn(false);

    userMessageSender.sendUserUpdatedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserDeletedMessageShouldSendToStreamBridgeWhenEventIsReceived() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();

    Mockito.when(userEventStreamPublisher.publishUserDeleted(event)).thenReturn(true);

    userMessageSender.sendUserDeletedMessage(event);

    Mockito.verify(userEventStreamPublisher).publishUserDeleted(event);
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void sendUserDeletedMessageShouldSaveOutboxEventWhenStreamBridgeFails() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();

    Mockito.when(userEventStreamPublisher.publishUserDeleted(event)).thenReturn(false);

    userMessageSender.sendUserDeletedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }
}
