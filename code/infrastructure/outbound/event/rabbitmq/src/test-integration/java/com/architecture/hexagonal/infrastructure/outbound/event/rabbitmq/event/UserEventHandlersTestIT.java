package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.producer.UserEventsProducerImpl;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(classes = {UserEventsProducerImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class UserEventHandlersTestIT {

  private static final UUID USER_ID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
  private static final String USER_NAME = "Test User";
  private static final String USER_EMAIL = "test@example.com";

  @Autowired private UserCreatedEventHandler userCreatedEventHandler;

  @Autowired private UserUpdatedEventHandler userUpdatedEventHandler;

  @Autowired private UserDeletedEventHandler userDeletedEventHandler;

  @Autowired private TransactionTemplate transactionTemplate;

  @MockitoBean private StreamBridge streamBridge;

  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Test
  void createdHandlerShouldPublishUserCreatedMessageAfterCommit() {
    final UserCreatedEvent event = new UserCreatedEvent();
    event.setUserId(USER_ID);
    event.setName(USER_NAME);
    event.setEmail(USER_EMAIL);
    Mockito.when(
            streamBridge.send(
                Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class)))
        .thenReturn(true);

    transactionTemplate.execute(
        status -> {
          userCreatedEventHandler.handle(event);
          return null;
        });

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-created-out-0"), Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void updatedHandlerShouldPublishUserUpdatedMessageAfterCommit() {
    final UserUpdatedEvent event = new UserUpdatedEvent();
    event.setUserId(USER_ID);
    event.setName(USER_NAME);
    event.setEmail(USER_EMAIL);
    Mockito.when(
            streamBridge.send(
                Mockito.eq("publish-user-updated-out-0"), Mockito.any(Message.class)))
        .thenReturn(true);

    transactionTemplate.execute(
        status -> {
          userUpdatedEventHandler.handle(event);
          return null;
        });

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-updated-out-0"), Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }

  @Test
  void deletedHandlerShouldPublishUserDeletedMessageAfterCommit() {
    final UserDeletedEvent event = new UserDeletedEvent();
    event.setUserId(USER_ID);
    event.setName(USER_NAME);
    event.setEmail(USER_EMAIL);
    Mockito.when(
            streamBridge.send(
                Mockito.eq("publish-user-deleted-out-0"), Mockito.any(Message.class)))
        .thenReturn(true);

    transactionTemplate.execute(
        status -> {
          userDeletedEventHandler.handle(event);
          return null;
        });

    Mockito.verify(streamBridge)
        .send(Mockito.eq("publish-user-deleted-out-0"), Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }
}
