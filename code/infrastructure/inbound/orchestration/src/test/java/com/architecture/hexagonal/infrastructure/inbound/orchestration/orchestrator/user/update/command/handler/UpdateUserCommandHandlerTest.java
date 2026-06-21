package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.update.command.handler;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserCommand;
import com.architecture.hexagonal.application.business.user.update.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UpdateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.UpdateUserCommandDtoTestDataBuilder;
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
class UpdateUserCommandHandlerTest {

  @InjectMocks private UpdateUserCommandHandler commandHandler;

  @Spy
  private UpdateUserCommandMapper updateUserCommandMapper =
      Mappers.getMapper(UpdateUserCommandMapper.class);

  @Mock private UpdateUserUseCase updateUserUseCase;

  @Test
  void handle_shouldReturnUpdatedUser_whenCommandIsExecuted() throws DomainException {
    UpdateUserCommandDto updateUserCommandDto =
        UpdateUserCommandDtoTestDataBuilder.builder().build().updateUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(updateUserUseCase.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
        .thenReturn(user);

    User result = commandHandler.handle(updateUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(updateUserCommandMapper).toUpdateUserCommand(updateUserCommandDto);
    Mockito.verify(updateUserUseCase).execute(ArgumentMatchers.any(UpdateUserCommand.class));
  }
}
