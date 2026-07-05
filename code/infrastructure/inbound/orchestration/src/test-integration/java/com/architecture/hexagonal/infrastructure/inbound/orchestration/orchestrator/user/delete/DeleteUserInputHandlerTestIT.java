package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.delete;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.application.business.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.DeleteUserCommandDtoTestDataBuilder;
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

@SpringBootTest(classes = DeleteUserCommandHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class DeleteUserInputHandlerTestIT {

  @Autowired
  private DeleteUserCommandHandlerImpl deleteUserCommandHandlerImpl;

  @MockitoSpyBean
  private DeleteUserCommandMapper deleteUserCommandMapper;

  @MockitoBean
  private DeleteUserUseCase deleteUserUseCase;

  @MockitoSpyBean
  private TransactionBoundary transactionBoundary;

  @Test
  void deleteUserCommandHandler_shouldReturnDeletedUser_whenCommandIsExecuted() {
    final DeleteUserCommandDto deleteUserCommandDto = DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(deleteUserUseCase.execute(ArgumentMatchers.any(DeleteUserInput.class)))
        .thenReturn(user);

    User result = deleteUserCommandHandlerImpl.handle(deleteUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(deleteUserCommandMapper).toDeleteUserCommand(deleteUserCommandDto);
    Mockito.verify(deleteUserUseCase).execute(ArgumentMatchers.any(DeleteUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }
}
