package com.architecture.hexagonal.infrastructure.inbound.bus.command.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.usecase.user.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
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

  @Mock private DeleteUserUseCaseImpl deleteUserUseCase;

  @Spy private final List<CommandHandler<?, ?>> commandHandlers = new ArrayList<>();

  @InjectMocks private CommandBusImpl commandBus;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final DeleteUserCommand command = new DeleteUserCommand();
    final User user = User.builder().build();

    Mockito.when(deleteUserUseCase.handle(command)).thenReturn(user);
    commandHandlers.add(deleteUserUseCase);
    commandBus = new CommandBusImpl(commandHandlers);

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserUseCase).handle(command);
  }
}
