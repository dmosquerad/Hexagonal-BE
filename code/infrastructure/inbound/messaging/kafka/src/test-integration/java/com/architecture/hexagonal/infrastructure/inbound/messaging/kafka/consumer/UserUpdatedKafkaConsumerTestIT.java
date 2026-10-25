package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserUpdatedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command.RegisterInboxCommandTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserUpdatedPayloadTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserUpdatedKafkaConsumer.class)
@ContextConfiguration(classes = TestApplication.class)
class UserUpdatedKafkaConsumerTestIT {

  @Autowired private UserUpdatedKafkaConsumer userUpdatedKafkaConsumer;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserUpdatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserUpdatedPayload message = UserUpdatedPayloadTestDataBuilder.builder().build().userUpdatedPayload();

    userUpdatedKafkaConsumer.listenUserUpdated(
        message, UserUpdatedConsumer.USER_UPDATED_TOPIC_ADDRESS);

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getUserId())
        .isEqualTo(message.getData_().getUserId());
    Assertions.assertThat(commandCaptor.getValue().getName())
        .isEqualTo(message.getData_().getName());
    Assertions.assertThat(commandCaptor.getValue().getEmail())
        .isEqualTo(message.getData_().getEmail());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserUpdatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserUpdatedPayload message = UserUpdatedPayloadTestDataBuilder.builder().build().userUpdatedPayload();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(UpdateUserCommand.class));

    userUpdatedKafkaConsumer.listenUserUpdated(
        message, UserUpdatedConsumer.USER_UPDATED_TOPIC_ADDRESS);

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
}
