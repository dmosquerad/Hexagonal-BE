package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.producer;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.naming.UserEventsProducerBinding;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
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
                Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_CREATED_OUT_0.getBindingName()),
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
                Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_UPDATED_OUT_0.getBindingName()),
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
                Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_DELETED_OUT_0.getBindingName()),
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
