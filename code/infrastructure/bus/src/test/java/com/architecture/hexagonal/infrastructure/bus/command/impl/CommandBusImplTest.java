package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.application.usecase.user.command.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.middleware.BusMiddleware;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.time.Clock;
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

  @Spy private BusMiddleware loggingMiddleware = new BusLoggingMiddleware(TestClock.FIXED_CLOCK);

  @InjectMocks private CommandBusImpl commandBus;

  @Mock private DeleteUserUseCaseImpl deleteUserUseCase;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final DeleteUserCommand command = new DeleteUserCommand();
    final User user = User.builder().build();

    Mockito.when(handlerFinder.find(command, CommandHandler.class)).thenReturn(deleteUserUseCase);
    Mockito.when(deleteUserUseCase.handle(command)).thenReturn(user);

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserUseCase).handle(command);
  }
}
