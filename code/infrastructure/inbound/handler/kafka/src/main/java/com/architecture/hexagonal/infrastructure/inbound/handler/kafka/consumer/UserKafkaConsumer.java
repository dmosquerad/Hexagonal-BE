package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.consumer;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper.UserKafkaMessageMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer {

  private final CommandBus commandBus;
  private final UserKafkaMessageMapper userKafkaMessageMapper;

  @KafkaListener(topics = "user.created", groupId = "user.handler")
  public void consumeUserCreated(final @NonNull @Payload UserCreatedMessage userCreatedMessage) {
    final CreateUserCommandDto command =
        userKafkaMessageMapper.toCreateUserCommand(userCreatedMessage);
    commandBus.execute(command);
  }

  @KafkaListener(topics = "user.updated", groupId = "user.handler")
  public void consumeUserUpdated(final @NonNull @Payload UserUpdatedMessage userUpdatedMessage) {
    final UpdateUserCommandDto command =
        userKafkaMessageMapper.toUpdateUserCommand(userUpdatedMessage);
    commandBus.execute(command);
  }

  @KafkaListener(topics = "user.deleted", groupId = "user.handler")
  public void consumeUserDeleted(final @NonNull @Payload UserDeletedMessage userDeletedMessage) {
    final DeleteUserCommandDto command =
        userKafkaMessageMapper.toDeleteUserCommand(userDeletedMessage);
    commandBus.execute(command);
  }
}
