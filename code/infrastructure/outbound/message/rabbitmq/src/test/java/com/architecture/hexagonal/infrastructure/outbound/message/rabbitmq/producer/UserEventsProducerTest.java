package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.producer;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.PayloadMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

@ExtendWith(MockitoExtension.class)
class UserEventsProducerTest {

  @InjectMocks private UserEventsProducerImpl publisher;

  @Mock private StreamBridge streamBridge;
  @Mock private PayloadMapper payloadMapper;
  @Mock private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Test
  void publishUserCreatedShouldSendOnBindingDeclaredByContract() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();
    Mockito.when(
            streamBridge.send(Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class)))
        .thenReturn(true);

    publisher.publishUserCreated(event);

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void publishUserUpdatedShouldSendOnBindingDeclaredByContract() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();
    Mockito.when(
            streamBridge.send(Mockito.eq("publish-user-updated-out-0"), Mockito.any(Message.class)))
        .thenReturn(true);

    publisher.publishUserUpdated(event);

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-updated-out-0"), Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void publishUserDeletedShouldSendOnBindingDeclaredByContract() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();
    Mockito.when(
            streamBridge.send(Mockito.eq("publish-user-deleted-out-0"), Mockito.any(Message.class)))
        .thenReturn(true);

    publisher.publishUserDeleted(event);

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-deleted-out-0"), Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void publishUserCreatedShouldSaveOutboxWhenStreamBridgeReturnsFalse() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();
    Mockito.when(
            streamBridge.send(Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class)))
        .thenReturn(false);

    assertThat(publisher.publishUserCreated(event)).isFalse();

    Mockito.verify(payloadMapper).toPayload(event);
    final ArgumentCaptor<Outbox> outboxCaptor = ArgumentCaptor.forClass(Outbox.class);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(outboxCaptor.capture());
    assertThat(outboxCaptor.getValue().aggregateId()).isEqualTo(event.getData().getUserId());
    assertThat(outboxCaptor.getValue().action()).isEqualTo("USER_CREATED");
  }

  @Test
  void publishUserCreatedShouldSaveOutboxWhenStreamBridgeThrows() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();
    Mockito.doThrow(new IllegalStateException("broker unavailable"))
        .when(streamBridge)
        .send(Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class));

    assertThat(publisher.publishUserCreated(event)).isFalse();

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void publishUserUpdatedShouldSaveOutboxWhenStreamBridgeReturnsFalse() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();
    Mockito.when(
            streamBridge.send(Mockito.eq("publish-user-updated-out-0"), Mockito.any(Message.class)))
        .thenReturn(false);

    assertThat(publisher.publishUserUpdated(event)).isFalse();

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }

  @Test
  void publishUserDeletedShouldSaveOutboxWhenStreamBridgeReturnsFalse() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();
    Mockito.when(
            streamBridge.send(Mockito.eq("publish-user-deleted-out-0"), Mockito.any(Message.class)))
        .thenReturn(false);

    assertThat(publisher.publishUserDeleted(event)).isFalse();

    Mockito.verify(payloadMapper).toPayload(event);
    Mockito.verify(outboxRepositoryWritePort).upsertByEventIdentity(Mockito.any(Outbox.class));
  }
}
