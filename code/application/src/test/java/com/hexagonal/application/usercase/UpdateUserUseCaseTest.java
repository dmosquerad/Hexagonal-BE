package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.UpdateUserCommandTestDataBuilder;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @InjectMocks
  UpdateUserUseCase updateUserUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void execute() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final UpdateUserCommand updateUserCommand = UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserCommand.getUserId()))
        .thenReturn(Optional.of(user));
    Mockito.when(userRepositoryWritePort.saveUser(user))
        .thenReturn(user);

    final User result = updateUserUseCase.execute(updateUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(updateUserCommand.getUserId());
    Mockito.verify(userRepositoryWritePort).saveUser(user);
  }

  @Test
  void executeUserNotFound() {
    final UpdateUserCommand updateUserCommand = UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(updateUserCommand.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> updateUserUseCase.execute(updateUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + updateUserCommand.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(updateUserCommand.getUserId());
    Mockito.verifyNoInteractions(userRepositoryWritePort);
  }

}