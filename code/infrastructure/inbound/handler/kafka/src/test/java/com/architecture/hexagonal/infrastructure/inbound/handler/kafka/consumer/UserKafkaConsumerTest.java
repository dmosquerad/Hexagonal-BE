package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.kafka.model.MessageHeader;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreated;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreatedData;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper.UserKafkaMessageMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserKafkaConsumerTest {

  @InjectMocks private UserKafkaConsumer userKafkaConsumer;
  @Mock private CommandBus commandBus;

  @Spy private Clock clock = Clock.systemUTC();

  @Spy
  private UserKafkaMessageMapper userKafkaMessageMapper =
      Mappers.getMapper(UserKafkaMessageMapper.class);

  @Test
  void consumeUserCreatedShouldExecuteCommandWithoutSavingInboxOnSuccess() {
    final UserCreated message = message();
    final CreateUserCommand command = org.mockito.Mockito.mock(CreateUserCommand.class);
    when(userKafkaMessageMapper.toCreateUserCommand(message)).thenReturn(command);

    userKafkaConsumer.onUserCreated(message, Map.of());

    verify(commandBus).execute(command);
  }

  @Test
  void consumeUserCreatedShouldRegisterInboxWhenCommandExecutionFails() {
    final OffsetDateTime before = OffsetDateTime.now();
    final UserCreated message = message();
    final CreateUserCommand command = org.mockito.Mockito.mock(CreateUserCommand.class);
    when(userKafkaMessageMapper.toCreateUserCommand(message)).thenReturn(command);
    doThrow(new IllegalStateException("command failed")).when(commandBus).execute(command);

    userKafkaConsumer.onUserCreated(message, Map.of());

    final ArgumentCaptor<RegisterInboxCommand> registerInboxCommandCaptor =
        ArgumentCaptor.forClass(RegisterInboxCommand.class);
    verify(commandBus).execute(registerInboxCommandCaptor.capture());
    verify(commandBus).execute(command);
    final RegisterInboxCommand registerCommand = registerInboxCommandCaptor.getValue();
    assertThat(registerCommand.getMessageHeader().getMessageId())
        .isEqualTo(message.getMessageHeader().getMessageId());
    assertThat(registerCommand.getMessageHeader().getMessageDate())
        .isEqualTo(message.getMessageHeader().getMessageDate().toString());
    assertThat(registerCommand.getAction()).isEqualTo("USER_CREATED");
    assertThat(registerCommand.getAggregateType()).isEqualTo("USER");
    assertThat(registerCommand.getData()).isSameAs(message);
    assertThat(registerCommand.getCreatedAt()).isBetween(before, OffsetDateTime.now());
  }

  private UserCreated message() {
    final MessageHeader header = new MessageHeader();
    header.setMessageId(UUID.randomUUID());
    header.setMessageDate(OffsetDateTime.parse("2026-10-13T10:15:30Z"));
    final UserCreated message = new UserCreated();
    message.setMessageHeader(header);
    message.setData(new UserCreatedData());
    return message;
  }
}
