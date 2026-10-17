package com.architecture.hexagonal.infrastructure.bus.command.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.usecase.user.create.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.bus.config.TestApplication;
import com.architecture.hexagonal.infrastructure.bus.MessageHandlerFinder;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.contract.application.usecase.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import java.util.UUID;
import java.util.function.Supplier;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

@SpringBootTest(
    classes = {CommandBusImpl.class, MessageHandlerFinder.class, CreateUserUseCaseImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class CommandBusImplTestIT {

  @Autowired private CommandBus commandBus;

  @MockitoBean private UserService userService;

  @MockitoBean private EmailService emailService;

  @MockitoBean private TransactionWriteBoundary transactionBoundary;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final CreateUserCommand command = new CreateUserCommand();
    command.setEmail("test@example.com");
    command.setName("Test User");
    final User user = User.builder().build();

    Mockito.when(transactionBoundary.execute(Mockito.any()))
        .thenAnswer(
            AdditionalAnswers.answer((Supplier<User> action) -> action.get()));
    Mockito.when(emailService.isEmailBlocked(Mockito.any())).thenReturn(false);
    Mockito.when(userService.createUser(Mockito.any())).thenReturn(user);

    final User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(userService).createUser(Mockito.any());
    Mockito.verify(transactionBoundary).execute(Mockito.<Supplier<User>>any());
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final DeleteUserCommand command = new DeleteUserCommand();
    command.setUserId(UUID.randomUUID());

    AssertionsForClassTypes.assertThatThrownBy(() -> commandBus.execute(command))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Expected one CommandHandler")
        .hasMessageContaining("found 0");
  }
}
