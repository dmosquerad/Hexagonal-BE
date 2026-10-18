package com.architecture.hexagonal.infrastructure.inbound.message.kafka.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.kafka.model.MessageHeader;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreated;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserDeleted;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserUpdated;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.message.kafka.config.TestApplication;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserKafkaConsumer.class)
@ContextConfiguration(classes = TestApplication.class)
class UserKafkaConsumerTestIT {

  @Autowired private UserKafkaConsumer userKafkaConsumer;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserCreatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserCreated message = userCreated();

    userKafkaConsumer.onUserCreated(message, Map.of());

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getName()).isEqualTo("Test User");
    assertThat(commandCaptor.getValue().getEmail()).isEqualTo("user@example.com");
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserCreatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserCreated message = userCreated();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(CreateUserCommand.class));

    userKafkaConsumer.onUserCreated(message, Map.of());

    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommand.class));
    assertInboxRegistered(
        message, "USER_CREATED", null, captureInboxCommand());
  }

  @Test
  void consumeUserUpdatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserUpdated message = userUpdated();

    userKafkaConsumer.onUserUpdated(message, Map.of());

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getUserId()).isEqualTo(message.getData().getUserId());
    assertThat(commandCaptor.getValue().getName()).isEqualTo(message.getData().getName());
    assertThat(commandCaptor.getValue().getEmail()).isEqualTo(message.getData().getEmail());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserUpdatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserUpdated message = userUpdated();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(UpdateUserCommand.class));

    userKafkaConsumer.onUserUpdated(message, Map.of());

    Mockito.verify(commandBus).execute(Mockito.any(UpdateUserCommand.class));
    assertInboxRegistered(
        message, "USER_UPDATED", message.getData().getUserId(), captureInboxCommand());
  }

  @Test
  void consumeUserDeletedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserDeleted message = userDeleted();

    userKafkaConsumer.onUserDeleted(message, Map.of());

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getUserId()).isEqualTo(message.getData().getUserId());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserDeletedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserDeleted message = userDeleted();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(DeleteUserCommand.class));

    userKafkaConsumer.onUserDeleted(message, Map.of());

    Mockito.verify(commandBus).execute(Mockito.any(DeleteUserCommand.class));
    assertInboxRegistered(
        message, "USER_DELETED", message.getData().getUserId(), captureInboxCommand());
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
    assertThat(command.getData()).isSameAs(message);
    assertThat(command.getMessageHeader().getMessageId()).isEqualTo(sourceHeader.getMessageId());
    assertThat(command.getMessageHeader().getMessageDate())
        .isEqualTo(sourceHeader.getMessageDate().toString());
    assertThat(command.getCreatedAt()).isNotNull();
  }

  private MessageHeader messageHeader(final Object message) {
    if (message instanceof UserCreated userCreated) {
      return userCreated.getMessageHeader();
    }
    if (message instanceof UserUpdated userUpdated) {
      return userUpdated.getMessageHeader();
    }
    return ((UserDeleted) message).getMessageHeader();
  }

  private UserCreated userCreated() {
    final UserCreatedData data = new UserCreatedData();
    data.setName("Test User");
    data.setEmail("user@example.com");
    final UserCreated message = new UserCreated();
    message.setMessageHeader(messageHeader());
    message.setData(data);
    return message;
  }

  private UserUpdated userUpdated() {
    final UserUpdatedData data = new UserUpdatedData();
    data.setUserId(UUID.randomUUID());
    data.setName("Updated User");
    data.setEmail("updated@example.com");
    final UserUpdated message = new UserUpdated();
    message.setMessageHeader(messageHeader());
    message.setData(data);
    return message;
  }

  private UserDeleted userDeleted() {
    final UserDeletedData data = new UserDeletedData();
    data.setUserId(UUID.randomUUID());
    final UserDeleted message = new UserDeleted();
    message.setMessageHeader(messageHeader());
    message.setData(data);
    return message;
  }

  private MessageHeader messageHeader() {
    final MessageHeader header = new MessageHeader();
    header.setMessageId(UUID.randomUUID());
    header.setMessageDate(OffsetDateTime.parse("2026-10-13T10:15:30Z"));
    return header;
  }
}
