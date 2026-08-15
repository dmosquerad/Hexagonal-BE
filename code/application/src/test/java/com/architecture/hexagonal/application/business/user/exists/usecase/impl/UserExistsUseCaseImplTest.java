package com.architecture.hexagonal.application.business.user.exists.usecase.impl;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.user.exists.input.UserExistsInputTestDataBuilder;
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
class UserExistsUseCaseImplTest {

  @InjectMocks UserExistsUseCaseImpl userExistsUseCaseImpl;

  @Mock UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute_shouldNotThrow_whenUserExists() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder.builder().build().user();

    final UserExistsInput userExistsInput =
        UserExistsInputTestDataBuilder.builder().build().userExistsInput();

    Mockito.when(userRepositoryReadPort.findUserById(userExistsInput.userId()))
        .thenReturn(Optional.of(user));

    userExistsUseCaseImpl.execute(userExistsInput);

    Mockito.verify(userRepositoryReadPort).findUserById(userExistsInput.userId());
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final UserExistsInput userExistsInput =
        UserExistsInputTestDataBuilder.builder().build().userExistsInput();

    Mockito.when(userRepositoryReadPort.findUserById(userExistsInput.userId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> userExistsUseCaseImpl.execute(userExistsInput))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userExistsInput.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(userExistsInput.userId());
  }
}
