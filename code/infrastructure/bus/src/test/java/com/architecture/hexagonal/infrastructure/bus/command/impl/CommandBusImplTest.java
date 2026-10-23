package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.usecase.user.command.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.StaticListableBeanFactory;

@ExtendWith(MockitoExtension.class)
class CommandBusImplTest {

  @Mock private DeleteUserUseCaseImpl deleteUserUseCase;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final DeleteUserCommand command = new DeleteUserCommand();
    final User user = User.builder().build();

    Mockito.when(deleteUserUseCase.handle(command)).thenReturn(user);
    final CommandBusImpl commandBus =
        new CommandBusImpl(finderWith(deleteUserUseCase), new BusLoggingMiddleware());

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserUseCase).handle(command);
  }

  @Test
  void executeShouldRejectMultipleHandlersForTheSameCommandType() {
    final BusHandlerFinder finder = finderWith(deleteUserUseCase, deleteUserUseCase);

    AssertionsForClassTypes.assertThatThrownBy(
            () -> finder.find(new DeleteUserCommand(), CommandHandler.class))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one CommandHandler")
        .hasMessageContaining("found 2");
  }

  private BusHandlerFinder finderWith(final Object... handlers) {
    final StaticListableBeanFactory beanFactory = new StaticListableBeanFactory();
    for (int index = 0; index < handlers.length; index++) {
      beanFactory.addBean("handler" + index, handlers[index]);
    }
    final BusHandlerFinder finder = new BusHandlerFinder(beanFactory);
    finder.afterSingletonsInstantiated();
    return finder;
  }
}
