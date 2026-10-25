package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserDeletedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command.RegisterInboxCommandTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserDeletedPayloadTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserDeletedKafkaConsumer.class)
@ContextConfiguration(classes = TestApplication.class)
class UserDeletedKafkaConsumerTestIT {

  @Autowired private UserDeletedKafkaConsumer userDeletedKafkaConsumer;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserDeletedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserDeletedPayload message = UserDeletedPayloadTestDataBuilder.builder().build().userDeletedPayload();

    userDeletedKafkaConsumer.listenUserDeleted(
        message, UserDeletedConsumer.USER_DELETED_TOPIC_ADDRESS);

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getUserId())
        .isEqualTo(message.getData_().getUserId());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserDeletedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserDeletedPayload message = UserDeletedPayloadTestDataBuilder.builder().build().userDeletedPayload();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(DeleteUserCommand.class));

    userDeletedKafkaConsumer.listenUserDeleted(
        message, UserDeletedConsumer.USER_DELETED_TOPIC_ADDRESS);

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
