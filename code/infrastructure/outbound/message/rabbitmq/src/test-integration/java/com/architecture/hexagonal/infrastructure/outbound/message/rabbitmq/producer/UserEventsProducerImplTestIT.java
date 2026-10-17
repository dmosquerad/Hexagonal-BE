package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.producer;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(classes = {UserEventsProducerImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class UserEventsProducerImplTestIT {

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private TransactionTemplate transactionTemplate;

  @MockitoBean private StreamBridge streamBridge;

  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @MockitoSpyBean private PayloadMapper payloadMapper;

  @Test
  void sendUserCreatedMessageShouldSaveOutboxEventWhenStreamBridgeThrows() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    Mockito.when(
            streamBridge.send(
                Mockito.eq("publish-user-created-out-0"),
                Mockito.any(org.springframework.messaging.Message.class)))
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

    Mockito.when(
            streamBridge.send(
                Mockito.eq("publish-user-updated-out-0"),
                Mockito.any(org.springframework.messaging.Message.class)))
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

    Mockito.when(
            streamBridge.send(
                Mockito.eq("publish-user-deleted-out-0"),
                Mockito.any(org.springframework.messaging.Message.class)))
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
