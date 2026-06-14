package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.delete.command.handler;

import com.architecture.hexagonal.application.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.application.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.delete.command.handler.DeleteUserCommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command.DeleteUserCommandDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = DeleteUserCommandHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class DeleteUserCommandHandlerTestIT {

  @Autowired
  private DeleteUserCommandHandler deleteUserCommandHandler;

  @MockitoSpyBean
  private DeleteUserCommandMapper deleteUserCommandMapper;

  @MockitoBean
  private DeleteUserUseCase deleteUserUseCase;

  @Test
  void deleteUserCommandHandler_shouldReturnDeletedUser_whenCommandIsExecuted() throws DomainException {
    final DeleteUserCommandDto deleteUserCommandDto = DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(deleteUserUseCase.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
        .thenReturn(user);

    User result = deleteUserCommandHandler.handle(deleteUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(deleteUserCommandMapper).toDeleteUserCommand(deleteUserCommandDto);
    Mockito.verify(deleteUserUseCase).execute(ArgumentMatchers.any(DeleteUserCommand.class));
  }
}
