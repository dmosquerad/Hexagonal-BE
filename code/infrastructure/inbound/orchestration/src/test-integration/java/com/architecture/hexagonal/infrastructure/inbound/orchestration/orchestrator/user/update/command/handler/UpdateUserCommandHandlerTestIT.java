package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.update.command.handler;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserCommand;
import com.architecture.hexagonal.application.business.user.update.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UpdateUserCommandMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.command.UpdateUserCommandDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@SpringBootTest(classes = UpdateUserCommandHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class UpdateUserCommandHandlerTestIT {

  @Autowired
  private UpdateUserCommandHandler updateUserCommandHandler;

  @MockitoSpyBean
  private UpdateUserCommandMapper updateUserCommandMapper;

  @MockitoBean
  private UpdateUserUseCase updateUserUseCase;

  @Test
  void updateUserCommandHandler_shouldReturnUpdatedUser_whenCommandIsExecuted() throws DomainException {
    final UpdateUserCommandDto updateUserCommandDto = UpdateUserCommandDtoTestDataBuilder.builder().build().updateUserCommandDto();
    final User user = Mockito.mock(User.class);

    Mockito.when(updateUserUseCase.execute(ArgumentMatchers.any(UpdateUserCommand.class)))
        .thenReturn(user);

    User result = updateUserCommandHandler.handle(updateUserCommandDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(user);

    Mockito.verify(updateUserCommandMapper).toUpdateUserCommand(updateUserCommandDto);
    Mockito.verify(updateUserUseCase).execute(ArgumentMatchers.any(UpdateUserCommand.class));
  }
}
