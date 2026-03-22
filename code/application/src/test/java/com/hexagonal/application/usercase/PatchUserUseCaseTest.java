package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.PatchUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.PatchUserCommandTestDataBuilder;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatchUserUseCaseTest {

  @InjectMocks
  PatchUserUseCase patchUserUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Mock
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

  @Test
  void executeUserNotFound() {
    final PatchUserCommand patchUserCommand = PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(patchUserCommand.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
        patchUserUseCase.execute(patchUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + patchUserCommand.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(patchUserCommand.getUserId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
  }
}