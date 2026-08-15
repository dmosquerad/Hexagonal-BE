package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.patch;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserInput;
import com.architecture.hexagonal.application.business.user.patch.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.PatchUserCommandDtoTestDataBuilder;
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
class PatchUserInputHandlerTest {

  @InjectMocks private PatchUserCommandHandlerImpl commandHandler;

  @Spy
  private PatchUserCommandMapper patchUserCommandMapper =
      Mappers.getMapper(PatchUserCommandMapper.class);

  @Mock private PatchUserUseCase patchUserUseCase;

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnPatchedUser_whenCommandIsExecuted() {
    PatchUserCommandDto patchUserCommandDto =
        PatchUserCommandDtoTestDataBuilder.builder().build().patchUserCommandDto();
    User user = UserTestDataBuilder.builder().build().user();
    Mockito.when(patchUserUseCase.execute(ArgumentMatchers.any(PatchUserInput.class)))
        .thenReturn(user);

    User result = commandHandler.handle(patchUserCommandDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(user);

    Mockito.verify(patchUserCommandMapper).toPatchUserCommand(patchUserCommandDto);
    Mockito.verify(patchUserUseCase).execute(ArgumentMatchers.any(PatchUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }
}
