package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.usecase.user.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.bus.MessageHandlerFinder;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandBusImplTest {

  @Mock private DeleteUserUseCaseImpl deleteUserUseCase;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final DeleteUserCommand command = new DeleteUserCommand();
    final User user = User.builder().build();

    Mockito.when(deleteUserUseCase.handle(command)).thenReturn(user);
    final CommandBusImpl commandBus =
        new CommandBusImpl(new MessageHandlerFinder(List.of(deleteUserUseCase), List.of()));

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserUseCase).handle(command);
  }

  @Test
  void executeShouldRejectMultipleHandlersForTheSameCommandType() {
    final MessageHandlerFinder finder =
        new MessageHandlerFinder(List.of(deleteUserUseCase, deleteUserUseCase), List.of());

    AssertionsForClassTypes.assertThatThrownBy(
            () -> finder.find(new DeleteUserCommand(), CommandHandler.class))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one CommandHandler")
        .hasMessageContaining("found 2");
  }
}
