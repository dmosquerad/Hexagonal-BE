package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserPublishBinding;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(classes = {UserMessageSender.class})
@ContextConfiguration(classes = TestApplication.class)
class UserMessageSenderTestIT {

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private TransactionTemplate transactionTemplate;

  @MockitoBean private StreamBridge streamBridge;

  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @MockitoSpyBean private PayloadMapper payloadMapper;

  @Test
  void sendUserCreatedMessageShouldSaveOutboxEventWhenStreamBridgeThrows() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_CREATED.getPublishBinding(), event))
        .thenThrow(new IllegalStateException("broker unavailable"));

    transactionTemplate.execute(
        status -> {
          applicationEventPublisher.publishEvent(event);
          return null;
        });

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserUpdatedMessageShouldSaveOutboxEventWhenStreamBridgeThrows() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_UPDATED.getPublishBinding(), event))
        .thenThrow(new IllegalStateException("broker unavailable"));

    transactionTemplate.execute(
        status -> {
          applicationEventPublisher.publishEvent(event);
          return null;
        });

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void sendUserDeletedMessageShouldSaveOutboxEventWhenStreamBridgeThrows() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();

    Mockito.when(streamBridge.send(UserPublishBinding.USER_DELETED.getPublishBinding(), event))
        .thenThrow(new IllegalStateException("broker unavailable"));

    transactionTemplate.execute(
        status -> {
          applicationEventPublisher.publishEvent(event);
          return null;
        });

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }
}
