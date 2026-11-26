package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.domain.port.in.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.usecase.user.command.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.model.command.user.CreateUserCommand;
import com.architecture.hexagonal.application.model.command.user.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.bus.BusHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.BusMessageValidator;
import com.architecture.hexagonal.infrastructure.bus.config.TestApplication;
import com.architecture.hexagonal.infrastructure.bus.middleware.impl.BusLoggingMiddleware;
import java.util.UUID;
import java.util.function.Supplier;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
    classes = {
      CommandBusImpl.class,
      BusHandlerFinder.class,
      BusMessageValidator.class,
      BusLoggingMiddleware.class,
      CreateUserUseCaseImpl.class
    })
@ContextConfiguration(classes = TestApplication.class)
class CommandBusImplTestIT {

  @Autowired private CommandBus commandBus;

  @MockitoBean private UserService userService;

  @MockitoBean private EmailService emailService;

  @MockitoBean private TransactionWriteBoundary transactionBoundary;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final CreateUserCommand command =
        CreateUserCommand.builder().email("test@example.com").name("Test User").build();
    final User user = User.builder().build();

    Mockito.when(transactionBoundary.execute(Mockito.any()))
        .thenAnswer(AdditionalAnswers.answer((Supplier<User> action) -> action.get()));
    Mockito.when(emailService.isEmailBlocked(Mockito.any())).thenReturn(false);
    Mockito.when(userService.createUser(Mockito.any())).thenReturn(user);

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(userService).createUser(Mockito.any());
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final DeleteUserCommand command = DeleteUserCommand.builder().userId(UUID.randomUUID()).build();

    AssertionsForClassTypes.assertThatThrownBy(() -> commandBus.execute(command))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one CommandHandler")
        .hasMessageContaining("found 0");
  }

  @Test
  void executeShouldRejectCommandsThatViolateContract() {
    final CreateUserCommand command = CreateUserCommand.builder().build();

    AssertionsForClassTypes.assertThatThrownBy(() -> commandBus.execute(command))
        .isInstanceOf(ConstraintViolationException.class);

    Mockito.verifyNoInteractions(userService);
  }
}
