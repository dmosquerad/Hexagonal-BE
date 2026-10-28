package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.client.consumer.UserCreatedConsumer;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command.RegisterInboxCommandTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserCreatedPayloadTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserCreatedKafkaConsumer.class)
@ContextConfiguration(classes = TestApplication.class)
class UserCreatedKafkaConsumerTestIT {

  @Autowired private UserCreatedKafkaConsumer userCreatedKafkaConsumer;

  @MockitoBean private CommandBus commandBus;

  @Test
  void consumeUserCreatedShouldMapCommandWithoutPersistingInboxOnSuccess() {
    final UserCreatedPayload message = UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();

    userCreatedKafkaConsumer.listenUserCreated(
        message, UserCreatedConsumer.USER_CREATED_TOPIC_ADDRESS);

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getName()).isEqualTo(message.getData_().getName());
    Assertions.assertThat(commandCaptor.getValue().getEmail()).isEqualTo(message.getData_().getEmail());
    Mockito.verify(commandBus, Mockito.never()).execute(Mockito.any(RegisterInboxCommand.class));
  }

  @Test
  void consumeUserCreatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserCreatedPayload message = UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();
    Mockito.doThrow(new IllegalStateException("command failed"))
        .when(commandBus)
        .execute(Mockito.any(CreateUserCommand.class));

    userCreatedKafkaConsumer.listenUserCreated(
        message, UserCreatedConsumer.USER_CREATED_TOPIC_ADDRESS);

    Mockito.verify(commandBus).execute(Mockito.any(CreateUserCommand.class));
    final ArgumentCaptor<RegisterInboxCommand> inboxCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    Mockito.verify(commandBus).execute(inboxCaptor.capture());
    final RegisterInboxCommand expected =
        RegisterInboxCommandTestDataBuilder.builder()
            .build()
            .registerInboxCommand();
    Assertions.assertThat(inboxCaptor.getValue())
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(expected);
  }
}
