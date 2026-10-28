package com.architecture.hexagonal.infrastructure.inbox.service;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbox.mapper.UserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.naming.InboxNaming;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class InboxProcessServiceTestIT {

  @InjectMocks private InboxProcessService inboxProcessService;

  @Mock private CommandBus commandBus;

  @Spy
  private UserCommandFromInboxMapper userCommandFromInboxMapper =
      Mappers.getMapper(UserCommandFromInboxMapper.class);

  @Spy private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void processShouldDispatchCreateCommandFromStoredPayload() {
    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(PayloadVoTestDataBuilder.<Object>builder().data(Map.of("name", "Inbox User", "email", "inbox@example.com")).build().payloadVo())
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getName()).isEqualTo("Inbox User");
    Assertions.assertThat(commandCaptor.getValue().getEmail()).isEqualTo("inbox@example.com");
  }

  @Test
  void processShouldDispatchUpdateCommandFromStoredPayload() {
    final UUID userId = UUID.randomUUID();
    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(PayloadVoTestDataBuilder.<Object>builder().data(Map.of(
                    "userId", userId.toString(),
                    "name", "Updated User",
                    "email", "updated@example.com")).build().payloadVo())
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getUserId()).isEqualTo(userId);
    Assertions.assertThat(commandCaptor.getValue().getName()).isEqualTo("Updated User");
    Assertions.assertThat(commandCaptor.getValue().getEmail()).isEqualTo("updated@example.com");
  }

  @Test
  void processShouldDispatchDeleteCommandFromStoredPayload() {
    final UUID userId = UUID.randomUUID();
    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(PayloadVoTestDataBuilder.<Object>builder().data(Map.of("userId", userId.toString())).build().payloadVo())
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getUserId()).isEqualTo(userId);
  }

  @Test
  void processShouldRejectUnsupportedUserAction() {
    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(PayloadVoTestDataBuilder.<Object>builder().data(Map.of("name", "Inbox User")).build().payloadVo())
            .action("UNKNOWN_ACTION")
            .build()
            .inbox();

    Assertions.assertThatThrownBy(() -> inboxProcessService.process(inbox))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_USER_ACTION + "UNKNOWN_ACTION");

    Mockito.verifyNoInteractions(commandBus);
  }
}
