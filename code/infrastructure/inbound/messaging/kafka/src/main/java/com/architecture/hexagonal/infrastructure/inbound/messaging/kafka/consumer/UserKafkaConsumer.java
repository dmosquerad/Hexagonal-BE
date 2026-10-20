package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.UserEventsConsumerService;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreated;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeleted;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdated;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UserKafkaMessageMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer implements UserEventsConsumerService {

  private final CommandBus commandBus;
  private final UserKafkaMessageMapper userKafkaMessageMapper;
  private final Clock clock;

  @Override
  @KafkaListener(topics = "user.created", groupId = "user.handler")
  public void onUserCreated(
      final UserCreated userCreatedMessage, final Map<String, Object> headers) {
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
  @KafkaListener(topics = "user.updated", groupId = "user.handler")
  public void onUserUpdated(
      final UserUpdated userUpdatedMessage, final Map<String, Object> headers) {
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
  @KafkaListener(topics = "user.deleted", groupId = "user.handler")
  public void onUserDeleted(
      final UserDeleted userDeletedMessage, final Map<String, Object> headers) {
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
