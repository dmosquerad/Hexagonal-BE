package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.impl;

import com.architecture.hexagonal.application.usecase.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.application.usecase.business.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.create.CreateUserCommandHandlerImpl;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.CreateUserCommandDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.DeleteUserCommandDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.Mockito;

@SpringBootTest(classes = {CommandBusImpl.class, CreateUserCommandHandlerImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class CommandBusImplTestIT {

  @Autowired private CommandBus commandBus;

  @MockitoSpyBean private CreateUserCommandMapper createUserCommandMapper;

  @MockitoBean private CreateUserUseCase createUserUseCase;

  @MockitoSpyBean private TransactionBoundary transactionBoundary;

  @Test
  void executeShouldDelegateCommandToCorrectHandler() {
    final CreateUserCommandDto command =
        CreateUserCommandDtoTestDataBuilder.builder().build().createUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(createUserUseCase.execute(Mockito.any(CreateUserInput.class))).thenReturn(user);

    User result = commandBus.execute(command);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(createUserCommandMapper).toCreateUserCommand(command);
    Mockito.verify(createUserUseCase).execute(Mockito.any(CreateUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }

  @Test
  void executeShouldThrowIllegalStateExceptionWhenNoHandlerIsFound() {
    final DeleteUserCommandDto command =
        DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();

    AssertionsForClassTypes.assertThatThrownBy(() -> commandBus.execute(command))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No handler found for command");
  }
}
