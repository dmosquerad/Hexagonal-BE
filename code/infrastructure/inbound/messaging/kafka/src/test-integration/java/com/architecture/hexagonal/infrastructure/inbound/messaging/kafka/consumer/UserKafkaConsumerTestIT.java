package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.MessageHeader;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.TestApplication;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
    classes = UserKafkaConsumer.class, properties = "spring.kafka.listener.auto-startup=false")
@ContextConfiguration(classes = TestApplication.class)
class UserKafkaConsumerTestIT {

  @Autowired private UserKafkaConsumer userKafkaConsumer;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserCreatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserCreatedPayload message = userCreated();

    userKafkaConsumer.listenUserCreated(message, "user.created");

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getName()).isEqualTo("Test User");
    assertThat(commandCaptor.getValue().getEmail()).isEqualTo("user@example.com");
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserCreatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserCreatedPayload message = userCreated();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(CreateUserCommand.class));

    userKafkaConsumer.listenUserCreated(message, "user.created");

    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommand.class));
    assertInboxRegistered(
        message, "USER_CREATED", null, captureInboxCommand());
  }

  @Test
  void consumeUserUpdatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserUpdatedPayload message = userUpdated();

    userKafkaConsumer.listenUserUpdated(message, "user.updated");

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getUserId()).isEqualTo(message.getData_().getUserId());
    assertThat(commandCaptor.getValue().getName()).isEqualTo(message.getData_().getName());
    assertThat(commandCaptor.getValue().getEmail()).isEqualTo(message.getData_().getEmail());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserUpdatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserUpdatedPayload message = userUpdated();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(UpdateUserCommand.class));

    userKafkaConsumer.listenUserUpdated(message, "user.updated");

    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommand.class));
    assertInboxRegistered(
        message, "USER_UPDATED", message.getData_().getUserId(), captureInboxCommand());
  }

  @Test
  void consumeUserDeletedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserDeletedPayload message = userDeleted();

    userKafkaConsumer.listenUserDeleted(message, "user.deleted");

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getUserId()).isEqualTo(message.getData_().getUserId());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserDeletedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserDeletedPayload message = userDeleted();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(DeleteUserCommand.class));

    userKafkaConsumer.listenUserDeleted(message, "user.deleted");

    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommand.class));
    assertInboxRegistered(
        message, "USER_DELETED", message.getData_().getUserId(), captureInboxCommand());
  }

  private RegisterInboxCommand captureInboxCommand() {
    final ArgumentCaptor<RegisterInboxCommand> commandCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    return commandCaptor.getValue();
  }

  private void assertInboxRegistered(
      final Object message,
      final String action,
      final UUID aggregateId,
      final RegisterInboxCommand command) {
    final MessageHeader sourceHeader = messageHeader(message);
    assertThat(command.getAction()).isEqualTo(action);
    assertThat(command.getAggregateType()).isEqualTo("USER");
    assertThat(command.getAggregateId()).isEqualTo(aggregateId);
    assertThat(command.getMessageHeader().getMessageId()).isEqualTo(sourceHeader.getMessageId());
    assertThat(command.getMessageHeader().getMessageDate())
        .isEqualTo(sourceHeader.getMessageDate().toString());
    assertThat(command.getCreatedAt()).isNotNull();
  }

  private MessageHeader messageHeader(final Object message) {
    if (message instanceof UserCreatedPayload userCreated) {
      return userCreated.getMessageHeader();
    }
    if (message instanceof UserUpdatedPayload userUpdated) {
      return userUpdated.getMessageHeader();
    }
    return ((UserDeletedPayload) message).getMessageHeader();
  }

  private UserCreatedPayload userCreated() {
    final UserCreatedData data = new UserCreatedData();
    data.setName("Test User");
    data.setEmail("user@example.com");
    final UserCreatedPayload message = new UserCreatedPayload();
    message.setMessageHeader(messageHeader());
    message.setData_(data);
    return message;
  }

  private UserUpdatedPayload userUpdated() {
    final UserUpdatedData data = new UserUpdatedData();
    data.setUserId(UUID.randomUUID());
    data.setName("Updated User");
    data.setEmail("updated@example.com");
    final UserUpdatedPayload message = new UserUpdatedPayload();
    message.setMessageHeader(messageHeader());
    message.setData_(data);
    return message;
  }

  private UserDeletedPayload userDeleted() {
    final UserDeletedData data = new UserDeletedData();
    data.setUserId(UUID.randomUUID());
    final UserDeletedPayload message = new UserDeletedPayload();
    message.setMessageHeader(messageHeader());
    message.setData_(data);
    return message;
  }

  private MessageHeader messageHeader() {
    final MessageHeader header = new MessageHeader();
    header.setMessageId(UUID.randomUUID());
    header.setMessageDate(OffsetDateTime.parse("2026-10-13T10:15:30Z"));
    return header;
  }
}
