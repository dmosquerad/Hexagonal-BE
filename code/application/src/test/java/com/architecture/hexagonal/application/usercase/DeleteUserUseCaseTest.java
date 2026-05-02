package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.cqrs.command.request.DeleteUserCommand;
import com.architecture.hexagonal.application.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.out.UserSenderPort;
import com.architecture.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.command.DeleteUserCommandTestDataBuilder;
import com.architecture.hexagonal.application.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

  @Mock
  UserSenderPort userSenderPort;

  @Test
  void execute_shouldDeleteUser_whenUserExists() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final DeleteUserCommand deleteUserCommand = DeleteUserCommandTestDataBuilder
        .builder()
        .build()
        .deleteUserCommand();

    Mockito.when(userRepositoryWritePort.deleteUser(deleteUserCommand.getUserId()))
        .thenReturn(Optional.of(user));

    final User result = deleteUserUseCase.execute(deleteUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).deleteUser(deleteUserCommand.getUserId());
    Mockito.verify(userSenderPort).userSenderDeleted(user);
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final DeleteUserCommand deleteUserCommand = DeleteUserCommandTestDataBuilder
        .builder()
        .build()
        .deleteUserCommand();

    Mockito.when(userRepositoryWritePort.deleteUser(deleteUserCommand.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
        deleteUserUseCase.execute(deleteUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + deleteUserCommand.getUserId());

    Mockito.verify(userRepositoryWritePort).deleteUser(deleteUserCommand.getUserId());
    Mockito.verifyNoInteractions(userSenderPort);
  }

}
