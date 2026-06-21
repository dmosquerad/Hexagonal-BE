package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.impl;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.delete.command.handler.DeleteUserCommandHandler;
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

  @Mock private DeleteUserCommandHandler deleteUserCommandHandler;

  @Spy private final List<CommandHandler<?, ?>> commandHandlers = new ArrayList<>();

  @InjectMocks private CommandBusImpl commandBus;

  @Test
  void execute_shouldDelegateCommandToCorrectHandler() throws DomainException {
    final DeleteUserCommandDto command =
        DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();
    final User user = UserTestDataBuilder.builder().build().user();

    Mockito.when(deleteUserCommandHandler.handle(command)).thenReturn(user);
    commandHandlers.add(deleteUserCommandHandler);
    commandBus = new CommandBusImpl(commandHandlers);

    User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserCommandHandler).handle(command);
  }

  @Test
  void execute_shouldThrowIllegalStateException_whenNoHandlerIsRegistered() {
    final String unknownCommand = "unknown";

    AssertionsForClassTypes.assertThatThrownBy(() -> commandBus.execute(unknownCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for command");
  }
}
