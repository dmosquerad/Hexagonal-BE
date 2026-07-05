package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.create;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.application.business.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.CreateUserCommandDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = CreateUserCommandHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class CreateUserInputHandlerTestIT {

  @Autowired
  private CreateUserCommandHandlerImpl createUserCommandHandlerImpl;

  @MockitoSpyBean
  private CreateUserCommandMapper createUserCommandMapper;

  @MockitoBean
  private CreateUserUseCase createUserUseCase;

  @MockitoSpyBean
  private TransactionBoundary transactionBoundary;

  @Test
  void createUserCommandHandler_shouldReturnCreatedUser_whenCommandIsExecuted() {
    final CreateUserCommandDto createUserCommandDto = CreateUserCommandDtoTestDataBuilder.builder().build().createUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(createUserUseCase.execute(ArgumentMatchers.any(CreateUserInput.class)))
        .thenReturn(user);

    User result = createUserCommandHandlerImpl.handle(createUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(createUserCommandMapper).toCreateUserCommand(createUserCommandDto);
    Mockito.verify(createUserUseCase).execute(ArgumentMatchers.any(CreateUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }
}
