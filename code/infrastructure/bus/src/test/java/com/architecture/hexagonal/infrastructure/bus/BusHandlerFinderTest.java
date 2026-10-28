package com.architecture.hexagonal.infrastructure.bus;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.usecase.user.command.delete.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.StaticListableBeanFactory;

@ExtendWith(MockitoExtension.class)
class BusHandlerFinderTest {

  @Mock private DeleteUserUseCaseImpl deleteUserUseCase;

  @Test
  void findShouldRejectMultipleHandlersForTheSameCommandType() {
    final StaticListableBeanFactory beanFactory = new StaticListableBeanFactory();
    beanFactory.addBean("firstHandler", deleteUserUseCase);
    beanFactory.addBean("secondHandler", deleteUserUseCase);
    final BusHandlerFinder handlerFinder = new BusHandlerFinder(beanFactory);
    handlerFinder.afterSingletonsInstantiated();

    AssertionsForClassTypes.assertThatThrownBy(
            () -> handlerFinder.find(new DeleteUserCommand(), CommandHandler.class))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one CommandHandler")
        .hasMessageContaining("found 2");
  }
}
