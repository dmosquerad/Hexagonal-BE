package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.feature.user.delete.command.DeleteUserCommand;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.command.DeleteUserCommandTestDataBuilder;
import com.architecture.hexagonal.application.feature.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = DeleteUserUseCase.class)
class DeleteUserUseCaseTestIT {

  @Autowired
  DeleteUserUseCase deleteUserUseCase;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @MockitoBean
  UserSenderPort userSenderPort;

  @Test
  void execute_shouldDeleteUser_whenUserExists() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final DeleteUserCommand deleteUserCommand = DeleteUserCommandTestDataBuilder
        .builder()
        .userId(user.getUser().getUserId())
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
  }
}
