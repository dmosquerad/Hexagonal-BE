package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserCreatedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserDeletedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserUpdatedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UserKafkaMessageMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer
    implements UserCreatedConsumer, UserUpdatedConsumer, UserDeletedConsumer {

  private final CommandBus commandBus;
  private final UserKafkaMessageMapper userKafkaMessageMapper;
  private final Clock clock;

  @Override
  @KafkaListener(topics = UserCreatedConsumer.USER_CREATED_TOPIC_ADDRESS)
  public void listenUserCreated(
      @Payload final UserCreatedPayload userCreatedMessage,
      @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = true) final String receivedTopic) {
    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(userKafkaMessageMapper.toCreateUserCommand(userCreatedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          userKafkaMessageMapper.toRegisterInboxCommand(userCreatedMessage);
      registerInboxCommand.setCreatedAt(currentDateTime);

      commandBus.execute(registerInboxCommand);
    }
  }

  @Override
  @KafkaListener(topics = UserUpdatedConsumer.USER_UPDATED_TOPIC_ADDRESS)
  public void listenUserUpdated(
      @Payload final UserUpdatedPayload userUpdatedMessage,
      @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = true) final String receivedTopic) {
    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(userKafkaMessageMapper.toUpdateUserCommand(userUpdatedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          userKafkaMessageMapper.toRegisterInboxCommand(userUpdatedMessage);
      registerInboxCommand.setCreatedAt(currentDateTime);

      commandBus.execute(registerInboxCommand);
    }
  }

  @Override
  @KafkaListener(topics = UserDeletedConsumer.USER_DELETED_TOPIC_ADDRESS)
  public void listenUserDeleted(
      @Payload final UserDeletedPayload userDeletedMessage,
      @Header(name = KafkaHeaders.RECEIVED_TOPIC, required = true) final String receivedTopic) {
    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(userKafkaMessageMapper.toDeleteUserCommand(userDeletedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          userKafkaMessageMapper.toRegisterInboxCommand(userDeletedMessage);
      registerInboxCommand.setCreatedAt(currentDateTime);

      commandBus.execute(registerInboxCommand);
    }
  }
}
