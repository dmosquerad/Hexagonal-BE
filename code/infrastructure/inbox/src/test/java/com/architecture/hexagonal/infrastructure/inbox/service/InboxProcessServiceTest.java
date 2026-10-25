package com.architecture.hexagonal.infrastructure.inbox.service;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.testutils.data.model.entity.inbox.InboxTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbox.mapper.UserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.naming.InboxNaming;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class InboxProcessServiceTest {

  private InboxProcessService inboxProcessService;

  @Mock private CommandBus commandBus;

  @Spy
  private final UserCommandFromInboxMapper userCommandFromInboxMapper =
      Mappers.getMapper(UserCommandFromInboxMapper.class);

  @Spy private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    inboxProcessService =
        Mockito.spy(new InboxProcessService(commandBus, userCommandFromInboxMapper, objectMapper));
  }

  @Test
  void processShouldDispatchCreateCommandForPreviouslyStoredUserData() {
    final UserCreatedData createdData = new UserCreatedData();
    createdData.setName("Inbox User");
    createdData.setEmail("inbox@example.com");

    final Inbox inbox =
        InboxTestDataBuilder.<Object>builder()
            .payload(
                PayloadVoTestDataBuilder.<Object>builder()
                    .data(Map.of("name", createdData.getName(), "email", createdData.getEmail()))
                    .build()
                    .payloadVo())
            .action(ActionType.UserActionType.USER_CREATED.getActionType())
            .build()
            .inbox();

    inboxProcessService.process(inbox);

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    Mockito.verify(commandBus).execute(commandCaptor.capture());
    Assertions.assertThat(commandCaptor.getValue().getName()).isEqualTo(createdData.getName());
    Assertions.assertThat(commandCaptor.getValue().getEmail()).isEqualTo(createdData.getEmail());
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
