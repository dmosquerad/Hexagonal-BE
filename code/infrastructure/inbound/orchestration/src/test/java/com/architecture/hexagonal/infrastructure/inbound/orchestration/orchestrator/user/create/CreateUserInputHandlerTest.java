package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.create;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.application.business.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.CreateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.CreateUserCommandDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.transaction.TransactionBoundaryTest;
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
class CreateUserInputHandlerTest {

  @InjectMocks private CreateUserCommandHandlerImpl commandHandler;

  @Spy
  private CreateUserCommandMapper createUserCommandMapper =
      Mappers.getMapper(CreateUserCommandMapper.class);

  @Mock private CreateUserUseCase createUserUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnCreatedUser_whenCommandIsExecuted() {
    CreateUserCommandDto createUserCommandDto =
        CreateUserCommandDtoTestDataBuilder.builder().build().createUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(createUserUseCase.execute(ArgumentMatchers.any(CreateUserInput.class)))
        .thenReturn(user);

    User result = commandHandler.handle(createUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(createUserCommandMapper).toCreateUserCommand(createUserCommandDto);
    Mockito.verify(createUserUseCase).execute(ArgumentMatchers.any(CreateUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }
}
