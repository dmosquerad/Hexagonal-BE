package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.patch.command.handler;

import com.architecture.hexagonal.application.user.patch.input.PatchUserCommand;
import com.architecture.hexagonal.application.user.patch.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.PatchUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.patch.command.handler.PatchUserCommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command.PatchUserCommandDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = PatchUserCommandHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class PatchUserCommandHandlerTestIT {

  @Autowired
  private PatchUserCommandHandler patchUserCommandHandler;

  @MockitoSpyBean
  private PatchUserCommandMapper patchUserCommandMapper;

  @MockitoBean
  private PatchUserUseCase patchUserUseCase;

  @Test
  void patchUserCommandHandler_shouldReturnPatchedUser_whenCommandIsExecuted() throws DomainException {
    final PatchUserCommandDto patchUserCommandDto = PatchUserCommandDtoTestDataBuilder.builder().build().patchUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(patchUserUseCase.execute(ArgumentMatchers.any(PatchUserCommand.class)))
        .thenReturn(user);

    User result = patchUserCommandHandler.handle(patchUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(patchUserCommandMapper).toPatchUserCommand(patchUserCommandDto);
    Mockito.verify(patchUserUseCase).execute(ArgumentMatchers.any(PatchUserCommand.class));
  }
}
