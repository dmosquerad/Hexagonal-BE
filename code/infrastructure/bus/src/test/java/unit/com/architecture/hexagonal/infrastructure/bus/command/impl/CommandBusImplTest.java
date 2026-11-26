package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.model.command.user.DeleteUserCommand;
import com.architecture.hexagonal.application.usecase.user.command.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.domain.port.in.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.BusMessageValidator;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
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

  @Mock private BusHandlerFinder handlerFinder;

  @Mock private BusMessageValidator messageValidator;

  @Spy private BusMiddleware loggingMiddleware = new BusLoggingMiddleware();

  @InjectMocks private CommandBusImpl commandBus;

  @Mock private DeleteUserUseCaseImpl deleteUserUseCase;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final DeleteUserCommand command =
        DeleteUserCommand.builder().userId(java.util.UUID.randomUUID()).build();
    final User user = User.builder().build();

    Mockito.when(handlerFinder.find(command, CommandHandler.class)).thenReturn(deleteUserUseCase);
    Mockito.when(deleteUserUseCase.handle(command)).thenReturn(user);

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(messageValidator).validate(command);
    Mockito.verify(deleteUserUseCase).handle(command);
  }
}
