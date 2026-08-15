package com.architecture.hexagonal.application.business.user.delete.usecase.impl;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.user.delete.input.DeleteUserInputTestDataBuilder;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

  @InjectMocks DeleteUserUseCaseImpl deleteUserUseCaseImpl;

  @Mock UserRepositoryWritePort userRepositoryWritePort;

  @Mock UserSenderPort userSenderPort;

  @Test
  void execute_shouldDeleteUser_whenUserExists() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder.builder().build().user();
    final DeleteUserInput deleteUserInput =
        DeleteUserInputTestDataBuilder.builder().build().deleteUserInput();

    Mockito.when(userRepositoryWritePort.deleteUser(deleteUserInput.userId()))
        .thenReturn(Optional.of(user));

    final User result = deleteUserUseCaseImpl.execute(deleteUserInput);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).deleteUser(deleteUserInput.userId());
    Mockito.verify(userSenderPort).userSenderDeleted(user);
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final DeleteUserInput deleteUserInput =
        DeleteUserInputTestDataBuilder.builder().build().deleteUserInput();

    Mockito.when(userRepositoryWritePort.deleteUser(deleteUserInput.userId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> deleteUserUseCaseImpl.execute(deleteUserInput))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + deleteUserInput.userId());

    Mockito.verify(userRepositoryWritePort).deleteUser(deleteUserInput.userId());
    Mockito.verifyNoInteractions(userSenderPort);
  }
}
