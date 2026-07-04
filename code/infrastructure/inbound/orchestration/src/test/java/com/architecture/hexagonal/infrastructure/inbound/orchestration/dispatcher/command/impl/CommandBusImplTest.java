package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.impl;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.delete.DeleteUserCommandHandlerImpl;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.DeleteUserCommandDtoTestDataBuilder;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandBusImplTest {

  @Mock private DeleteUserCommandHandlerImpl deleteUserCommandHandlerImpl;

  @Spy private final List<CommandHandler<?, ?>> commandHandlers = new ArrayList<>();

  @InjectMocks private CommandBusImpl commandBus;

  @Test
  void execute_shouldDelegateCommandToCorrectHandler() {
    final DeleteUserCommandDto command =
        DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(deleteUserCommandHandlerImpl.handle(command)).thenReturn(user);
    commandHandlers.add(deleteUserCommandHandlerImpl);
    commandBus = new CommandBusImpl(commandHandlers);

    User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserCommandHandlerImpl).handle(command);
  }

  @Test
  void execute_shouldThrowIllegalStateException_whenNoHandlerIsRegistered() {
    final String unknownCommand = "unknown";

    AssertionsForClassTypes.assertThatThrownBy(() -> commandBus.execute(unknownCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for command");
  }
}
