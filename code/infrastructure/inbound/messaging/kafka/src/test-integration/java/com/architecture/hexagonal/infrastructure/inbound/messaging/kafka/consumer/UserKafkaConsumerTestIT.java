package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserEventsConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.router.UserMessageConsumerBinding;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer.impl.UserKafkaConsumerImpl;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command.RegisterInboxCommandTestDataBuilder;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserKafkaConsumerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class UserKafkaConsumerTestIT {

  @Autowired private UserKafkaConsumerImpl userKafkaConsumer;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserCreatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserCreatedPayload message =
        UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();

    userKafkaConsumer.listenUserCreated(
        message,
        UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
        UserMessageConsumerBinding.USER_CREATE_EVENT.getBindingName(),
        UUID.randomUUID(),
        "1");

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    final CreateUserCommand expected = new CreateUserCommand();
    expected.setName(message.getData_().getName());
    expected.setEmail(message.getData_().getEmail());
    Assertions.assertThat(commandCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expected);
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserCreatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserCreatedPayload message =
        UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(CreateUserCommand.class));

    userKafkaConsumer.listenUserCreated(
        message,
        UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
        UserMessageConsumerBinding.USER_CREATE_EVENT.getBindingName(),
        UUID.randomUUID(),
        "1");

    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommand.class));
    final ArgumentCaptor<RegisterInboxCommand> inboxCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    Mockito.verify(commandBus).execute(inboxCaptor.capture());
    final RegisterInboxCommand expected =
        RegisterInboxCommandTestDataBuilder.builder().build().registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue())
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(expected);
  }

  @Test
  void consumeUserUpdatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserUpdatedPayload message =
        UserUpdatedPayloadTestDataBuilder.builder().build().userUpdatedPayload();

    userKafkaConsumer.listenUserUpdated(
        message,
        UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
        message.getData_().getUserId(),
        UserMessageConsumerBinding.USER_UPDATE_EVENT.getBindingName(),
        UUID.randomUUID(),
        "1");

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    final UpdateUserCommand expected = new UpdateUserCommand();
    expected.setUserId(message.getData_().getUserId());
    expected.setName(message.getData_().getName());
    expected.setEmail(message.getData_().getEmail());
    Assertions.assertThat(commandCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expected);
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserUpdatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserUpdatedPayload message =
        UserUpdatedPayloadTestDataBuilder.builder().build().userUpdatedPayload();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(UpdateUserCommand.class));

    userKafkaConsumer.listenUserUpdated(
        message,
        UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
        message.getData_().getUserId(),
        UserMessageConsumerBinding.USER_UPDATE_EVENT.getBindingName(),
        UUID.randomUUID(),
        "1");

    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommand.class));
    final ArgumentCaptor<RegisterInboxCommand> inboxCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    Mockito.verify(commandBus).execute(inboxCaptor.capture());
    final RegisterInboxCommand expected =
        RegisterInboxCommandTestDataBuilder.builder()
            .data(message.getData_())
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .aggregateId(message.getData_().getUserId())
            .build()
            .registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue())
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(expected);
  }

  @Test
  void consumeUserDeletedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserDeletedPayload message =
        UserDeletedPayloadTestDataBuilder.builder().build().userDeletedPayload();

    userKafkaConsumer.listenUserDeleted(
        message,
        UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
        message.getData_().getUserId(),
        UserMessageConsumerBinding.USER_DELETE_EVENT.getBindingName(),
        UUID.randomUUID(),
        "1");

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    final DeleteUserCommand expected = new DeleteUserCommand();
    expected.setUserId(message.getData_().getUserId());
    Assertions.assertThat(commandCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expected);
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserDeletedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserDeletedPayload message =
        UserDeletedPayloadTestDataBuilder.builder().build().userDeletedPayload();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(DeleteUserCommand.class));

    userKafkaConsumer.listenUserDeleted(
        message,
        UserEventsConsumer.USER_EVENTS_TOPIC_ADDRESS,
        message.getData_().getUserId(),
        UserMessageConsumerBinding.USER_DELETE_EVENT.getBindingName(),
        UUID.randomUUID(),
        "1");

    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommand.class));
    final ArgumentCaptor<RegisterInboxCommand> inboxCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    Mockito.verify(commandBus).execute(inboxCaptor.capture());
    final RegisterInboxCommand expected =
        RegisterInboxCommandTestDataBuilder.builder()
            .data(message.getData_())
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .aggregateId(message.getData_().getUserId())
            .build()
            .registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue())
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(expected);
  }
}
