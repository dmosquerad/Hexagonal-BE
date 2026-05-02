package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.create.command.handler;

import com.architecture.hexagonal.application.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.application.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command.CreateUserCommandDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserCommandHandlerTest {

  @InjectMocks private CreateUserCommandHandler commandHandler;

  @Spy
  private CreateUserCommandMapper createUserCommandMapper =
      Mappers.getMapper(CreateUserCommandMapper.class);

  @Mock private CreateUserUseCase createUserUseCase;

  @Test
  void handle_shouldReturnCreatedUser_whenCommandIsExecuted() throws DomainException {
    CreateUserCommandDto createUserCommandDto =
        CreateUserCommandDtoTestDataBuilder.builder().build().createUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(createUserUseCase.execute(ArgumentMatchers.any(CreateUserCommand.class)))
        .thenReturn(user);

    User result = commandHandler.handle(createUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(createUserCommandMapper).toCreateUserCommand(createUserCommandDto);
    Mockito.verify(createUserUseCase).execute(ArgumentMatchers.any(CreateUserCommand.class));
  }
}
