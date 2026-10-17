package com.architecture.hexagonal.infrastructure.inbox.service;

import static org.mockito.Mockito.verify;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbox.mapper.UserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.naming.InboxNaming;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class InboxProcessServiceTest {

  @Mock private CommandBus commandBus;
  @Mock private ObjectProvider<CommandBus> commandBusProvider;

  private InboxProcessService inboxProcessService;
  private UserCreatedData createdData;
  private UserUpdatedData updatedData;
  private UserDeletedData deletedData;

  @BeforeEach
  void setUp() {
    inboxProcessService =
        new InboxProcessService(
            commandBusProvider,
            Mappers.getMapper(UserCommandFromInboxMapper.class),
            new ObjectMapper());
    createdData = new UserCreatedData();
    createdData.setName("Inbox User");
    createdData.setEmail("inbox@example.com");
    updatedData = new UserUpdatedData();
    updatedData.setUserId(UUID.randomUUID());
    updatedData.setName("Inbox User");
    updatedData.setEmail("inbox@example.com");
    deletedData = new UserDeletedData();
    deletedData.setUserId(updatedData.getUserId());
  }

  @Test
  void processShouldDispatchCreateCommandForPreviouslyStoredUserData() {
    Mockito.when(commandBusProvider.getObject()).thenReturn(commandBus);
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
    final InboxProcessService service = Mockito.spy(inboxProcessService);
    Mockito.when(service.aggregateHandlers()).thenReturn(Map.of());

    Assertions.assertThatThrownBy(service::validateAggregateHandlers)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_AGGREGATE_TYPE);
  }

  @Test
  void processShouldThrowWhenAggregateHandlerIsMissing() {
    final InboxProcessService service = Mockito.spy(inboxProcessService);
    Mockito.when(service.aggregateHandlers()).thenReturn(Map.of());

    Assertions.assertThatThrownBy(() -> service.process(inbox("USER_CREATED", createdData)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(InboxNaming.UNSUPPORTED_AGGREGATE_TYPE);

    Mockito.verifyNoInteractions(commandBus);
  }

  @Test
  void processShouldThrowWhenActionIsUnsupported() {
    Assertions.assertThatThrownBy(() -> inboxProcessService.process(inbox("", createdData)))
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
