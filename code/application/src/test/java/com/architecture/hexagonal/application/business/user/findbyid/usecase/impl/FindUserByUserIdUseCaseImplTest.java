package com.architecture.hexagonal.application.business.user.findbyid.usecase.impl;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.user.findbyid.input.FindUserByUserIdInputTestDataBuilder;
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
class FindUserByUserIdUseCaseImplTest {

  @InjectMocks FindUserByUserIdUseCaseImpl findUserByUserIdUseCaseImpl;

  @Mock UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute_shouldReturnUser_whenUserExists() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder.builder().build().user();
    final FindUserByUserIdInput findUserByUserIdInput =
        FindUserByUserIdInputTestDataBuilder.builder().build().findUserByUserIdInput();

    Mockito.when(userRepositoryReadPort.findUserById(findUserByUserIdInput.userId()))
        .thenReturn(Optional.of(user));

    User result = findUserByUserIdUseCaseImpl.execute(findUserByUserIdInput);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(findUserByUserIdInput.userId());
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final FindUserByUserIdInput findUserByUserIdInput =
        FindUserByUserIdInputTestDataBuilder.builder().build().findUserByUserIdInput();

    Mockito.when(userRepositoryReadPort.findUserById(findUserByUserIdInput.userId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(
            () -> findUserByUserIdUseCaseImpl.execute(findUserByUserIdInput))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + findUserByUserIdInput.userId());

    Mockito.verify(userRepositoryReadPort).findUserById(findUserByUserIdInput.userId());
  }
}
