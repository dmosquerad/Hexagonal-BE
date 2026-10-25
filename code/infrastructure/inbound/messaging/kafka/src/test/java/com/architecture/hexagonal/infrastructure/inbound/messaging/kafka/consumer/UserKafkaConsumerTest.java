package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.consumer;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.mapper.UserKafkaMessageMapper;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.command.RegisterInboxCommandTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message.UserCreatedPayloadTestDataBuilder;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
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
class UserKafkaConsumerTest {

  @InjectMocks private UserKafkaConsumer userKafkaConsumer;
  @Mock private CommandBus commandBus;

  @Spy private Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);

  @Spy
  private UserKafkaMessageMapper userKafkaMessageMapper =
      Mappers.getMapper(UserKafkaMessageMapper.class);

  @Test
  void consumeUserCreatedShouldExecuteCommandWithoutSavingInboxOnSuccess() {
    final UserCreatedPayload message =
        UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();
    final CreateUserCommand command = Mockito.mock(CreateUserCommand.class);
    Mockito.when(userKafkaMessageMapper.toCreateUserCommand(message)).thenReturn(command);

    userKafkaConsumer.listenUserCreated(message, "user.created");

    Mockito.verify(commandBus).execute(command);
  }

  @Test
  void consumeUserCreatedShouldRegisterInboxWhenCommandExecutionFails() {
    final UserCreatedPayload message =
        UserCreatedPayloadTestDataBuilder.builder().build().userCreatedPayload();
    final CreateUserCommand command = Mockito.mock(CreateUserCommand.class);
    Mockito.when(userKafkaMessageMapper.toCreateUserCommand(message)).thenReturn(command);
    Mockito.doThrow(new IllegalStateException("command failed")).when(commandBus).execute(command);

    userKafkaConsumer.listenUserCreated(message, "user.created");

    final ArgumentCaptor<RegisterInboxCommand> registerInboxCommandCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    Mockito.verify(commandBus).execute(registerInboxCommandCaptor.capture());
    Mockito.verify(commandBus).execute(command);
    final RegisterInboxCommand expected =
        RegisterInboxCommandTestDataBuilder.builder().build().registerInboxCommand();
    Assertions.assertThat(registerInboxCommandCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }
}
