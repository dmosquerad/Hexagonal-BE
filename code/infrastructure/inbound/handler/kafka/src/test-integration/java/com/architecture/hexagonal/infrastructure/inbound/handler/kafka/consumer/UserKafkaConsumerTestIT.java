package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.consumer;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper.UserKafkaMessageMapper;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message.UserCreatedMessageTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message.UserDeletedMessageTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message.UserUpdatedMessageTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(classes = UserKafkaConsumer.class)
@ContextConfiguration(classes = TestApplication.class)
class UserKafkaConsumerTestIT {

  @Autowired private UserKafkaConsumer userKafkaConsumer;

  @MockitoSpyBean private UserKafkaMessageMapper userKafkaMessageMapper;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserCreated_shouldExecuteCreateCommand_whenMessageIsReceived() {
    final UserCreatedMessage message =
        UserCreatedMessageTestDataBuilder.builder().build().userCreatedMessage();

    userKafkaConsumer.consumeUserCreated(message);

    Mockito.verify(userKafkaMessageMapper).toCreateUserCommand(message);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(CreateUserCommandDto.class));
  }

  @Test
  void consumeUserUpdated_shouldExecuteUpdateCommand_whenMessageIsReceived() {
    final UserUpdatedMessage message =
        UserUpdatedMessageTestDataBuilder.builder().build().userUpdatedMessage();

    userKafkaConsumer.consumeUserUpdated(message);

    Mockito.verify(userKafkaMessageMapper).toUpdateUserCommand(message);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(UpdateUserCommandDto.class));
  }

  @Test
  void consumeUserDeleted_shouldExecuteDeleteCommand_whenMessageIsReceived() {
    final UserDeletedMessage message =
        UserDeletedMessageTestDataBuilder.builder().build().userDeletedMessage();

    userKafkaConsumer.consumeUserDeleted(message);

    Mockito.verify(userKafkaMessageMapper).toDeleteUserCommand(message);
    Mockito.verify(commandBus).execute(ArgumentMatchers.any(DeleteUserCommandDto.class));
  }
}
