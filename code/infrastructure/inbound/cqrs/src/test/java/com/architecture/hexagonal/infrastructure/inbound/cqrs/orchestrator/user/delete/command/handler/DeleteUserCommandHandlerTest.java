package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.delete.command.handler;

import com.architecture.hexagonal.application.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.application.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.DeleteUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command.DeleteUserCommandDtoTestDataBuilder;
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
class DeleteUserCommandHandlerTest {

  @InjectMocks private DeleteUserCommandHandler commandHandler;

  @Spy
  private DeleteUserCommandMapper deleteUserCommandMapper =
      Mappers.getMapper(DeleteUserCommandMapper.class);

  @Mock private DeleteUserUseCase deleteUserUseCase;

  @Test
  void handle_shouldReturnDeletedUser_whenCommandIsExecuted() throws DomainException {
    DeleteUserCommandDto deleteUserCommandDto =
        DeleteUserCommandDtoTestDataBuilder.builder().build().deleteUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(deleteUserUseCase.execute(ArgumentMatchers.any(DeleteUserCommand.class)))
        .thenReturn(user);

    User result = commandHandler.handle(deleteUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(deleteUserCommandMapper).toDeleteUserCommand(deleteUserCommandDto);
    Mockito.verify(deleteUserUseCase).execute(ArgumentMatchers.any(DeleteUserCommand.class));
  }
}
