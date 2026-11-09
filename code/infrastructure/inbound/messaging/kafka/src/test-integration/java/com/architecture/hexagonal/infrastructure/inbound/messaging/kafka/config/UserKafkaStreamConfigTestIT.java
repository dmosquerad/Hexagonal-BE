package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.header.HeaderNaming;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.router.UserMessageConsumerBinding;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer.impl.UserKafkaConsumerImpl;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserCreatedPayloadTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserDeletedPayloadTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserUpdatedPayloadTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserKafkaConsumerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class UserKafkaStreamConfigTestIT {

  @Autowired private InputDestination inputDestination;

  @MockitoBean private CommandBus commandBus;

  @Test
  void createEventShouldBeDispatchedToCreateUserCommand() {
    final UserCreatedPayload message =
        UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();

    inputDestination.send(
        MessageBuilder.withPayload(message)
            .setHeader(HeaderNaming.EVENT_TYPE_HEADER, UserMessageConsumerBinding.USER_CREATE_EVENT.getBindingName())
            .setHeader(HeaderNaming.EVENT_ID_HEADER, UUID.randomUUID().toString())
            .setHeader(HeaderNaming.SCHEMA_VERSION_HEADER, "1")
            .build());

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getEmail())
        .isEqualTo(message.getData_().getEmail());
  }

  @Test
  void updateEventShouldBeDispatchedToUpdateUserCommand() {
    final UserUpdatedPayload message =
        UserUpdatedPayloadTestDataBuilder.builder().build().userUpdatedPayload();

    inputDestination.send(
        MessageBuilder.withPayload(message)
            .setHeader(HeaderNaming.EVENT_TYPE_HEADER, UserMessageConsumerBinding.USER_UPDATE_EVENT.getBindingName())
            .setHeader(HeaderNaming.EVENT_ID_HEADER, UUID.randomUUID().toString())
            .setHeader(HeaderNaming.SCHEMA_VERSION_HEADER, "1")
            .build());

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getUserId())
        .isEqualTo(message.getData_().getUserId());
  }

  @Test
  void deleteEventShouldBeDispatchedToDeleteUserCommand() {
    final UserDeletedPayload message =
        UserDeletedPayloadTestDataBuilder.builder().build().userDeletedPayload();

    inputDestination.send(
        MessageBuilder.withPayload(message)
            .setHeader(HeaderNaming.EVENT_TYPE_HEADER, UserMessageConsumerBinding.USER_DELETE_EVENT.getBindingName())
            .setHeader(HeaderNaming.EVENT_ID_HEADER, UUID.randomUUID().toString())
            .setHeader(HeaderNaming.SCHEMA_VERSION_HEADER, "1")
            .build());

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getUserId())
        .isEqualTo(message.getData_().getUserId());
  }
}
