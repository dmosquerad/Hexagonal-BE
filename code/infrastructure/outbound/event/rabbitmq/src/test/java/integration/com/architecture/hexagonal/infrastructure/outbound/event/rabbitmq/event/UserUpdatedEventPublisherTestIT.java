package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.naming.router.UserEventsProducerBinding;
import com.architecture.hexagonal.domain.port.out.repository.outbox.OutboxRepositoryWritePort;
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
class UserUpdatedEventPublisherTestIT {

  @Autowired private UserUpdatedEventPublisher userUpdatedEventHandler;
  @Autowired private TransactionTemplate transactionTemplate;
  @MockitoBean private StreamBridge streamBridge;
  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Test
  void updatedHandlerShouldPublishUserUpdatedMessageAfterCommit() {
    final UserUpdatedEvent event = new UserUpdatedEvent();
    event.setUserId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
    event.setName("Test User");
    event.setEmail("test@example.com");
    Mockito.when(
            streamBridge.send(
                Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_UPDATED_OUT_0.getBindingName()),
                Mockito.any(Message.class)))
        .thenReturn(true);

    transactionTemplate.execute(
        status -> {
          userUpdatedEventHandler.publish(event);
          return null;
        });

    Mockito.verify(streamBridge)
        .send(
            Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_UPDATED_OUT_0.getBindingName()),
            Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }
}
