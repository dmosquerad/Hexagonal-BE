package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.consumer;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper.UserKafkaMessageMapper;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message.UserCreatedMessageTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message.UserDeletedMessageTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message.UserUpdatedMessageTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserKafkaConsumerTest {

  @InjectMocks private UserKafkaConsumer userKafkaConsumer;

  @Mock private CommandBus commandBus;

  @Spy
  private UserKafkaMessageMapper userKafkaMessageMapper =
      Mappers.getMapper(UserKafkaMessageMapper.class);

  @Test
  void consumeUserCreated_shouldExecuteCreateCommand_whenMessageIsReceived() {
    final UserCreatedMessage message =
        UserCreatedMessageTestDataBuilder.builder().build().userCreatedMessage();

    userKafkaConsumer.consumeUserCreated(message);

    Mockito.verify(userKafkaMessageMapper).toCreateUserCommand(message);
    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommandDto.class));
  }

  @Test
  void consumeUserUpdated_shouldExecuteUpdateCommand_whenMessageIsReceived() {
    final UserUpdatedMessage message =
        UserUpdatedMessageTestDataBuilder.builder().build().userUpdatedMessage();

    userKafkaConsumer.consumeUserUpdated(message);

    Mockito.verify(userKafkaMessageMapper).toUpdateUserCommand(message);
    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommandDto.class));
  }

  @Test
  void consumeUserDeleted_shouldExecuteDeleteCommand_whenMessageIsReceived() {
    final UserDeletedMessage message =
        UserDeletedMessageTestDataBuilder.builder().build().userDeletedMessage();

    userKafkaConsumer.consumeUserDeleted(message);

    Mockito.verify(userKafkaMessageMapper).toDeleteUserCommand(message);
    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommandDto.class));
  }
}
