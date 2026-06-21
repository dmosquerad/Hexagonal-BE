package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.patch;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserInput;
import com.architecture.hexagonal.application.business.user.patch.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.PatchUserCommandDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = PatchUserCommandHandlerImpl.class)
@ContextConfiguration(classes = TestApplication.class)
class PatchUserInputHandlerTestIT {

  @Autowired
  private PatchUserCommandHandlerImpl patchUserCommandHandlerImpl;

  @MockitoSpyBean
  private PatchUserCommandMapper patchUserCommandMapper;

  @MockitoBean
  private PatchUserUseCase patchUserUseCase;

  @MockitoSpyBean
  private TransactionBoundary transactionBoundary;

  @Test
  void patchUserCommandHandler_shouldReturnPatchedUser_whenCommandIsExecuted() {
    final PatchUserCommandDto patchUserCommandDto = PatchUserCommandDtoTestDataBuilder.builder().build().patchUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(patchUserUseCase.execute(ArgumentMatchers.any(PatchUserInput.class)))
        .thenReturn(user);

    User result = patchUserCommandHandlerImpl.handle(patchUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(patchUserCommandMapper).toPatchUserCommand(patchUserCommandDto);
    Mockito.verify(patchUserUseCase).execute(ArgumentMatchers.any(PatchUserInput.class));
    Mockito.verify(transactionBoundary).write(Mockito.any());
  }
}
