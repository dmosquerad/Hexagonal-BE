package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.PatchUserCommandTestDataBuilder;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(user));
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    final User result = patchUserUseCase.execute(PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userRepositoryWritePort).saveUser(user);
  }

  @Test
  void executeUserNotFound() {
    final var patchUserCommand = PatchUserCommandTestDataBuilder
        .builder()
        .build()
        .patchUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
        patchUserUseCase.execute(patchUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + patchUserCommand.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userRepositoryWritePort, Mockito.never()).saveUser(ArgumentMatchers.any(User.class));
  }
}