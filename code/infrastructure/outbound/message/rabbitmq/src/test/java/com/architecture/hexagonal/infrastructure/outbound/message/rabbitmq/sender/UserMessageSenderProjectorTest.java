package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserPublishBinding;
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
import org.springframework.cloud.stream.function.StreamBridge;

@ExtendWith(MockitoExtension.class)
class UserMessageSenderProjectorTest {

  @InjectMocks UserMessageSender userMessageSender;

  @Mock StreamBridge streamBridge;
  @Mock OutboxRepositoryWritePort outboxRepositoryWritePort;
  @Spy private PayloadMapper payloadMapper = Mappers.getMapper(PayloadMapper.class);

  @Test
  void sendUserCreatedMessage_shouldSendToStreamBridge_whenEventIsReceived() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_CREATED.getPublishBinding(), event))
        .thenReturn(true);

    userMessageSender.sendUserCreatedMessage(event);

    Mockito.verify(streamBridge).send(UserPublishBinding.USER_CREATED.getPublishBinding(), event);
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void sendUserCreatedMessage_shouldSaveOutboxEvent_whenStreamBridgeFails() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_CREATED.getPublishBinding(), event))
        .thenReturn(false);

    userMessageSender.sendUserCreatedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).save(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserUpdatedMessage_shouldSendToStreamBridge_whenEventIsReceived() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_UPDATED.getPublishBinding(), event))
        .thenReturn(true);

    userMessageSender.sendUserUpdatedMessage(event);

    Mockito.verify(streamBridge).send(UserPublishBinding.USER_UPDATED.getPublishBinding(), event);
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void sendUserUpdatedMessage_shouldSaveOutboxEvent_whenStreamBridgeFails() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_UPDATED.getPublishBinding(), event))
        .thenReturn(false);

    userMessageSender.sendUserUpdatedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).save(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserDeletedMessage_shouldSendToStreamBridge_whenEventIsReceived() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_DELETED.getPublishBinding(), event))
        .thenReturn(true);

    userMessageSender.sendUserDeletedMessage(event);

    Mockito.verify(streamBridge).send(UserPublishBinding.USER_DELETED.getPublishBinding(), event);
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void sendUserDeletedMessage_shouldSaveOutboxEvent_whenStreamBridgeFails() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_DELETED.getPublishBinding(), event))
        .thenReturn(false);

    userMessageSender.sendUserDeletedMessage(event);

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).save(Mockito.any(Outbox.class));
  }
}
