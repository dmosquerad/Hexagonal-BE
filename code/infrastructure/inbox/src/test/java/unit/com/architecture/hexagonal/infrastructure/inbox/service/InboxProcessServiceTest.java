package com.architecture.hexagonal.infrastructure.inbox.service;

import com.architecture.hexagonal.application.model.command.user.CreateUserCommand;
import com.architecture.hexagonal.application.model.command.user.DeleteUserCommand;
import com.architecture.hexagonal.application.model.command.user.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.domain.databuilder.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbox.mapper.CreateUserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.mapper.DeleteUserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.mapper.UpdateUserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.naming.InboxNaming;
import java.util.Map;
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
class InboxProcessServiceTest {

  @Spy @InjectMocks private InboxProcessService inboxProcessService;

  @Mock private CommandBus commandBus;

  @Spy
  private final CreateUserCommandFromInboxMapper createUserCommandFromInboxMapper =
      Mappers.getMapper(CreateUserCommandFromInboxMapper.class);

  @Spy
  private final UpdateUserCommandFromInboxMapper updateUserCommandFromInboxMapper =
      Mappers.getMapper(UpdateUserCommandFromInboxMapper.class);

  @Spy
  private final DeleteUserCommandFromInboxMapper deleteUserCommandFromInboxMapper =
      Mappers.getMapper(DeleteUserCommandFromInboxMapper.class);

  @Test
  void processShouldDispatchCreateCommandForPreviouslyStoredUserData() {
    final UserCreatedData createdData = new UserCreatedData();
    createdData.setName("Inbox User");
    createdData.setEmail("inbox@example.com");

    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder().data(createdData).build().payloadVo())
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    final CreateUserCommand expected =
        CreateUserCommand.builder()
            .name(createdData.getName())
            .email(createdData.getEmail())
            .build();
    Assertions.assertThat(commandCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void processShouldDispatchUpdateCommandForPreviouslyStoredUserData() {
    final UUID userId = UUID.randomUUID();
    final UserUpdatedData updatedData = new UserUpdatedData();
    updatedData.setUserId(userId);
    updatedData.setName("Updated Inbox User");
    updatedData.setEmail("updated-inbox@example.com");

    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder().data(updatedData).build().payloadVo())
            .action(ActionType.UserActionType.USER_UPDATED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<UpdateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    final UpdateUserCommand expected =
        UpdateUserCommand.builder()
            .userId(userId)
            .name(updatedData.getName())
            .email(updatedData.getEmail())
            .build();
    Assertions.assertThat(commandCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void processShouldDispatchDeleteCommandForPreviouslyStoredUserData() {
    final UUID userId = UUID.randomUUID();
    final UserDeletedData deletedData = new UserDeletedData();
    deletedData.setUserId(userId);

    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder().data(deletedData).build().payloadVo())
            .action(ActionType.UserActionType.USER_DELETED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<DeleteUserCommand> commandCaptor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    final DeleteUserCommand expected = DeleteUserCommand.builder().userId(userId).build();
    Assertions.assertThat(commandCaptor.getValue()).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void shouldHaveHandlerForEveryAggregateType() {
    Assertions.assertThatCode(inboxProcessService::validateAggregateHandlers)
        .doesNotThrowAnyException();
  }

  @Test
  void shouldThrowWhenAggregateHandlerIsMissing() {
    Mockito.when(inboxProcessService.aggregateHandlers()).thenReturn(Map.of());

    Assertions.assertThatThrownBy(inboxProcessService::validateAggregateHandlers)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_AGGREGATE_TYPE);
  }

  @Test
  void processShouldThrowWhenAggregateHandlerIsMissing() {
    Mockito.when(inboxProcessService.aggregateHandlers()).thenReturn(Map.of());

    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .data(Map.of("name", "Inbox User"))
                    .build()
                    .payloadVo())
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .build()
            .inbox();

    Assertions.assertThatThrownBy(() -> inboxProcessService.process(inbox))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_AGGREGATE_TYPE);

    Mockito.verifyNoInteractions(commandBus);
  }

  @Test
  void processShouldThrowWhenActionIsUnsupported() {
    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(PayloadVoTestDataBuilder.<Object>builder().data(Map.of()).build().payloadVo())
            .action("")
            .build()
            .inbox();

    Assertions.assertThatThrownBy(() -> inboxProcessService.process(inbox))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_USER_ACTION);

    Mockito.verifyNoInteractions(commandBus);
  }
}
