package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.patch.command.handler;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserCommand;
import com.architecture.hexagonal.application.business.user.patch.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.PatchUserCommandDtoTestDataBuilder;
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
class PatchUserCommandHandlerTest {

  @InjectMocks private PatchUserCommandHandler commandHandler;

  @Spy
  private PatchUserCommandMapper patchUserCommandMapper =
      Mappers.getMapper(PatchUserCommandMapper.class);

  @Mock private PatchUserUseCase patchUserUseCase;

  @Test
  void handle_shouldReturnPatchedUser_whenCommandIsExecuted() throws DomainException {
    PatchUserCommandDto patchUserCommandDto =
        PatchUserCommandDtoTestDataBuilder.builder().build().patchUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(patchUserUseCase.execute(ArgumentMatchers.any(PatchUserCommand.class)))
        .thenReturn(user);

    User result = commandHandler.handle(patchUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(patchUserCommandMapper).toPatchUserCommand(patchUserCommandDto);
    Mockito.verify(patchUserUseCase).execute(ArgumentMatchers.any(PatchUserCommand.class));
  }
}
