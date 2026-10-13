package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.consumer;

import com.architecture.hexagonal.application.port.in.bus.command.CommandBus;
import com.architecture.hexagonal.contract.application.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.application.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.application.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper.UserKafkaMessageMapper;
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
    final CreateUserCommand command =
        userKafkaMessageMapper.toCreateUserCommand(userCreatedMessage);
    commandBus.execute(command);
  }

  @KafkaListener(topics = "user.updated", groupId = "user.handler")
  public void consumeUserUpdated(final @NonNull @Payload UserUpdatedMessage userUpdatedMessage) {
    final UpdateUserCommand command =
        userKafkaMessageMapper.toUpdateUserCommand(userUpdatedMessage);
    commandBus.execute(command);
  }

  @KafkaListener(topics = "user.deleted", groupId = "user.handler")
  public void consumeUserDeleted(final @NonNull @Payload UserDeletedMessage userDeletedMessage) {
    final DeleteUserCommand command =
        userKafkaMessageMapper.toDeleteUserCommand(userDeletedMessage);
    commandBus.execute(command);
  }
}
