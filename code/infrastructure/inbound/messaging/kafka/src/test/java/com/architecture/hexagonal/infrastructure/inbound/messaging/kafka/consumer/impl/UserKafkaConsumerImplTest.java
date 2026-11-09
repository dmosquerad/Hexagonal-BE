package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer.impl;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserEventsConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.router.UserMessageConsumerBinding;
import com.architecture.hexagonal.domain.data.time.TestClock;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UserKafkaMessageMapper;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command.RegisterInboxCommandTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserCreatedPayloadTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserDeletedPayloadTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserUpdatedPayloadTestDataBuilder;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserKafkaConsumerImplTest {

  @InjectMocks private UserKafkaConsumerImpl userKafkaConsumer;

  @Mock private CommandBus commandBus;

  @Spy
  private UserKafkaMessageMapper userKafkaMessageMapper =
      Mappers.getMapper(UserKafkaMessageMapper.class);

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void consumeUserCreatedShouldExecuteCommandWithoutSavingInboxOnSuccess() {
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
    Assertions.assertThat(commandCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
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
        RegisterInboxCommandTestDataBuilder.builder()
            .data(message.getData_())
            .createdAt(OffsetDateTime.now(clock))
            .build()
            .registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void consumeUserUpdatedShouldExecuteCommandWithoutSavingInboxOnSuccess() {
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
    Assertions.assertThat(commandCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
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
            .aggregateId(message.getData_().getUserId())
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .createdAt(OffsetDateTime.now(clock))
            .build()
            .registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void consumeUserDeletedShouldExecuteCommandWithoutSavingInboxOnSuccess() {
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
    Assertions.assertThat(commandCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
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
            .aggregateId(message.getData_().getUserId())
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .createdAt(OffsetDateTime.now(clock))
            .build()
            .registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }
}
