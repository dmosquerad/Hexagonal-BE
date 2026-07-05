package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.delete;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.application.business.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.DeleteUserCommandDtoTestDataBuilder;
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
class DeleteUserInputHandlerTest {

  @InjectMocks private DeleteUserCommandHandlerImpl commandHandler;

  @Spy
  private DeleteUserCommandMapper deleteUserCommandMapper =
      Mappers.getMapper(DeleteUserCommandMapper.class);

  @Mock private DeleteUserUseCase deleteUserUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnDeletedUser_whenCommandIsExecuted() {
    DeleteUserCommandDto deleteUserCommandDto =
        DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(deleteUserUseCase.execute(ArgumentMatchers.any(DeleteUserInput.class)))
        .thenReturn(user);

    User result = commandHandler.handle(deleteUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserCommandMapper).toDeleteUserCommand(deleteUserCommandDto);
    Mockito.verify(deleteUserUseCase).execute(ArgumentMatchers.any(DeleteUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }
}
