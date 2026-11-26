package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.domain.model.event.user.UserCreatedEvent;
import com.architecture.hexagonal.domain.databuilder.model.event.user.UserCreatedEventTestDataBuilder;
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
class UserCreatedEventPublisherTestIT {

  @Autowired private UserCreatedEventPublisher userCreatedEventHandler;
  @Autowired private TransactionTemplate transactionTemplate;
  @MockitoBean private StreamBridge streamBridge;
  @MockitoBean private OutboxRepositoryWritePort outboxRepositoryWritePort;

  @Test
  void createdHandlerShouldPublishUserCreatedMessageAfterCommit() {
    final UserCreatedEvent event =
        UserCreatedEventTestDataBuilder.builder()
            .userId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
            .build()
            .userCreatedEvent();
    Mockito.when(
            streamBridge.send(
                Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_CREATED_OUT_0.getBindingName()),
                Mockito.any(Message.class)))
        .thenReturn(true);

    transactionTemplate.execute(
        status -> {
          userCreatedEventHandler.publish(event);
          return null;
        });

    Mockito.verify(streamBridge)
        .send(
            Mockito.eq(UserEventsProducerBinding.PUBLISH_USER_CREATED_OUT_0.getBindingName()),
            Mockito.any(Message.class));
    Mockito.verifyNoInteractions(outboxRepositoryWritePort);
  }
}
