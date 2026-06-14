package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.create.command.handler;

import com.architecture.hexagonal.application.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.application.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.create.command.handler.CreateUserCommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command.CreateUserCommandDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = CreateUserCommandHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class CreateUserCommandHandlerTestIT {

  @Autowired
  private CreateUserCommandHandler createUserCommandHandler;

  @MockitoSpyBean
  private CreateUserCommandMapper createUserCommandMapper;

  @MockitoBean
  private CreateUserUseCase createUserUseCase;

  @Test
  void createUserCommandHandler_shouldReturnCreatedUser_whenCommandIsExecuted() throws DomainException {
    final CreateUserCommandDto createUserCommandDto = CreateUserCommandDtoTestDataBuilder.builder().build().createUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(createUserUseCase.execute(ArgumentMatchers.any(CreateUserCommand.class)))
        .thenReturn(user);

    User result = createUserCommandHandler.handle(createUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(createUserCommandMapper).toCreateUserCommand(createUserCommandDto);
    Mockito.verify(createUserUseCase).execute(ArgumentMatchers.any(CreateUserCommand.class));
  }
}
