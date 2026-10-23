package com.architecture.hexagonal.infrastructure.inbox.service;

import static org.mockito.Mockito.verify;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
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

    inboxProcessService.process(
        inbox(
            "USER_CREATED",
            Map.of("name", createdData.getName(), "email", createdData.getEmail())));

    final ArgumentCaptor<CreateUserCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    verify(commandBus).execute(commandCaptor.capture());
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

    Assertions.assertThatThrownBy(
            () -> inboxProcessService.process(inbox("USER_CREATED", Map.of("name", "Inbox User"))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_AGGREGATE_TYPE);

    Mockito.verifyNoInteractions(commandBus);
  }

  @Test
  void processShouldThrowWhenActionIsUnsupported() {
    Assertions.assertThatThrownBy(() -> inboxProcessService.process(inbox("", Map.of())))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_USER_ACTION);

    Mockito.verifyNoInteractions(commandBus);
  }

  private Inbox inbox(final String action, final Object message) {
    return Inbox.builder()
        .payload(
            PayloadVo.builder()
                .messageHeader(MessageHeaderVo.builder().build())
                .data(message)
                .build())
        .aggregateType(AggregateTypeVo.USER)
        .action(action)
        .build();
  }
}
