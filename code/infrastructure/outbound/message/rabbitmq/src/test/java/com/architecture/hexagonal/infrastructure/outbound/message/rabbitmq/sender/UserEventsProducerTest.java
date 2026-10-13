package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender;

import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserEventsProducer;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;

@ExtendWith(MockitoExtension.class)
class UserEventsProducerTest {

  @InjectMocks private UserEventsProducer publisher;

  @Mock private StreamBridge streamBridge;

  @Mock private ApplicationContext applicationContext;

  @Test
  void publishUserCreatedShouldSendOnBindingDeclaredByContract() {
    final UserCreated event = new UserCreated();
    publisher.publishUserCreated(event);

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class));
  }

  @Test
  void publishUserUpdatedShouldSendOnBindingDeclaredByContract() {
    final UserUpdated event = new UserUpdated();
    publisher.publishUserUpdated(event);

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-updated-out-0"), Mockito.any(Message.class));
  }

  @Test
  void publishUserDeletedShouldSendOnBindingDeclaredByContract() {
    final UserDeleted event = new UserDeleted();
    publisher.publishUserDeleted(event);

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-deleted-out-0"), Mockito.any(Message.class));
  }
}
