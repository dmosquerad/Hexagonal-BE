package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserEventsConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.header.HeaderNaming;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.router.UserMessageConsumerBinding;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer.impl.UserKafkaConsumerImpl;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
@RequiredArgsConstructor
public class UserKafkaStreamConfig {

  private final UserKafkaConsumerImpl userKafkaConsumer;

  @Bean(UserMessageConsumerBinding.Naming.USER_CREATE_ACTION)
  public Consumer<Message<UserCreatedPayload>> userCreate() {
    return message ->
        userKafkaConsumer.listenUserCreated(
            message.getPayload(),
            UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
            String.valueOf(message.getHeaders().get(HeaderNaming.EVENT_TYPE_HEADER)),
            UUID.fromString(String.valueOf(message.getHeaders().get(HeaderNaming.EVENT_ID_HEADER))),
            String.valueOf(message.getHeaders().get(HeaderNaming.SCHEMA_VERSION_HEADER)));
  }

  @Bean(UserMessageConsumerBinding.Naming.USER_UPDATE_ACTION)
  public Consumer<Message<UserUpdatedPayload>> userUpdate() {
    return message -> {
      final UserUpdatedPayload payload = message.getPayload();
      userKafkaConsumer.listenUserUpdated(
          payload,
          UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
          payload.getData_().getUserId(),
          String.valueOf(message.getHeaders().get(HeaderNaming.EVENT_TYPE_HEADER)),
          UUID.fromString(String.valueOf(message.getHeaders().get(HeaderNaming.EVENT_ID_HEADER))),
          String.valueOf(message.getHeaders().get(HeaderNaming.SCHEMA_VERSION_HEADER)));
    };
  }

  @Bean(UserMessageConsumerBinding.Naming.USER_DELETE_ACTION)
  public Consumer<Message<UserDeletedPayload>> userDelete() {
    return message -> {
      final UserDeletedPayload payload = message.getPayload();
      userKafkaConsumer.listenUserDeleted(
          payload,
          UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
          payload.getData_().getUserId(),
          String.valueOf(message.getHeaders().get(HeaderNaming.EVENT_TYPE_HEADER)),
          UUID.fromString(String.valueOf(message.getHeaders().get(HeaderNaming.EVENT_ID_HEADER))),
          String.valueOf(message.getHeaders().get(HeaderNaming.SCHEMA_VERSION_HEADER)));
    };
  }
}
