package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.PatchUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.PatchUserCommandTestDataBuilder;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = PatchUserUseCase.class)
class PatchUserUseCaseTestIT {

  @Autowired
  PatchUserUseCase patchUserUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void executeNamePatch() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final PatchUserCommand patchUserCommand = PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(patchUserCommand.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(userRepositoryWritePort.saveUser(user))
        .thenReturn(user);

    final User result = patchUserUseCase.execute(patchUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(patchUserCommand.getUserId());
    Mockito.verify(userRepositoryWritePort).saveUser(user);
  }
}