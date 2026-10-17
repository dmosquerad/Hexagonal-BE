package com.architecture.hexagonal.infrastructure.inbox.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.architecture.hexagonal.contract.application.usecase.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbox.mapper.UserCommandFromInboxMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import tools.jackson.databind.ObjectMapper;

class InboxProcessServiceTestIT {

  private CommandBus commandBus;
  private ObjectProvider<CommandBus> commandBusProvider;
  private InboxProcessService inboxProcessService;

  @BeforeEach
  void setUp() {
    commandBus = Mockito.mock(CommandBus.class);
    commandBusProvider = Mockito.mock(ObjectProvider.class);
    Mockito.when(commandBusProvider.getObject()).thenReturn(commandBus);
    inboxProcessService =
        new InboxProcessService(
            commandBusProvider,
            Mappers.getMapper(UserCommandFromInboxMapper.class),
            new ObjectMapper());
    inboxProcessService.validateAggregateHandlers();
  }

  @Test
  void processShouldDispatchCreateCommandFromStoredPayload() {
    inboxProcessService.process(
        inbox("USER_CREATED", Map.of("name", "Inbox User", "email", "inbox@example.com")));

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getName()).isEqualTo("Inbox User");
    assertThat(commandCaptor.getValue().getEmail()).isEqualTo("inbox@example.com");
  }

  @Test
  void processShouldDispatchUpdateCommandFromStoredPayload() {
    final UUID userId = UUID.randomUUID();
    inboxProcessService.process(
        inbox(
            "USER_UPDATED",
            Map.of(
                "userId", userId.toString(),
                "name", "Updated User",
                "email", "updated@example.com")));

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getUserId()).isEqualTo(userId);
    assertThat(commandCaptor.getValue().getName()).isEqualTo("Updated User");
    assertThat(commandCaptor.getValue().getEmail()).isEqualTo("updated@example.com");
  }

  @Test
  void processShouldDispatchDeleteCommandFromStoredPayload() {
    final UUID userId = UUID.randomUUID();
    inboxProcessService.process(
        inbox("USER_DELETED", Map.of("userId", userId.toString())));

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    verify(commandBus).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue().getUserId()).isEqualTo(userId);
  }

  @Test
  void processShouldRejectUnsupportedUserAction() {
    assertThatThrownBy(
            () ->
                inboxProcessService.process(
                    inbox("UNKNOWN_ACTION", Map.of("name", "Inbox User"))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unsupported user action: UNKNOWN_ACTION");

    verifyNoInteractions(commandBus);
  }

  private Inbox inbox(final String action, final Object data) {
    return Inbox.builder()
        .payload(
            PayloadVo.builder()
                .messageHeader(MessageHeaderVo.builder().build())
                .data(data)
                .build())
        .aggregateType(AggregateTypeVo.USER)
        .action(action)
        .build();
  }
}
