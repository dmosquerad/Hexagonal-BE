package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.DeleteUserCommandTestDataBuilder;
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
class DeleteUserUseCaseTest {

  @InjectMocks
  DeleteUserUseCase deleteUserUseCase;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void execute() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userRepositoryWritePort.deleteUser(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(user));

    final User result = deleteUserUseCase.execute(DeleteUserCommandTestDataBuilder
        .builder()
        .build()
        .deleteUserCommand());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).deleteUser(ArgumentMatchers.any(UUID.class));
  }

  @Test
  void executeUserNotFound() {
    final DeleteUserCommand deleteUserCommand = DeleteUserCommandTestDataBuilder
        .builder()
        .build()
        .deleteUserCommand();

    Mockito.when(userRepositoryWritePort.deleteUser(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
        deleteUserUseCase.execute(deleteUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + deleteUserCommand.getUserId());

    Mockito.verify(userRepositoryWritePort).deleteUser(ArgumentMatchers.any(UUID.class));
  }

}
