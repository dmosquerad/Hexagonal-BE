package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer.impl;

import com.architecture.hexagonal.application.model.command.inbox.RegisterInboxCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserEventsConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.KafkaClockConfig;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.RegisterInboxCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UpdateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.converter.DateConverter;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumerImpl implements UserEventsConsumer {

  private final CommandBus commandBus;
  private final CreateUserCommandMapper createUserCommandMapper;
  private final UpdateUserCommandMapper updateUserCommandMapper;
  private final DeleteUserCommandMapper deleteUserCommandMapper;
  private final RegisterInboxCommandMapper registerInboxCommandMapper;

  @Qualifier(KafkaClockConfig.KAFKA_CLOCK)
  private final Clock clock;

  @Override
  public void listenUserCreated(
      final UserCreatedPayload userCreatedMessage,
      final String receivedTopic,
      final String xEventType,
      final UUID xEventId,
      final String xSchemaVersion) {
    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(createUserCommandMapper.toCreateUserCommand(userCreatedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          registerInboxCommandMapper.toRegisterInboxCommand(userCreatedMessage).toBuilder()
              .createdAt(DateConverter.toDate(currentDateTime))
              .build();

      commandBus.execute(registerInboxCommand);
    }
  }

  @Override
  public void listenUserUpdated(
      final UserUpdatedPayload userUpdatedMessage,
      final String receivedTopic,
      final UUID receivedKey,
      final String xEventType,
      final UUID xEventId,
      final String xSchemaVersion) {

    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(updateUserCommandMapper.toUpdateUserCommand(userUpdatedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          registerInboxCommandMapper.toRegisterInboxCommand(userUpdatedMessage).toBuilder()
              .createdAt(DateConverter.toDate(currentDateTime))
              .build();

      commandBus.execute(registerInboxCommand);
    }
  }

  @Override
  public void listenUserDeleted(
      final UserDeletedPayload userDeletedMessage,
      final String receivedTopic,
      final UUID receivedKey,
      final String xEventType,
      final UUID xEventId,
      final String xSchemaVersion) {

    final OffsetDateTime currentDateTime = OffsetDateTime.now(clock);

    try {
      commandBus.execute(deleteUserCommandMapper.toDeleteUserCommand(userDeletedMessage));
    } catch (final Exception _) {
      final RegisterInboxCommand registerInboxCommand =
          registerInboxCommandMapper.toRegisterInboxCommand(userDeletedMessage).toBuilder()
              .createdAt(DateConverter.toDate(currentDateTime))
              .build();

      commandBus.execute(registerInboxCommand);
    }
  }
}
