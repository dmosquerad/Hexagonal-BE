package com.architecture.hexagonal.application.feature.user.exists.usecase;

import com.architecture.hexagonal.application.feature.user.exists.query.UserExistsQuery;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.feature.user.exists.query.UserExistsQueryTestDataBuilder;
import com.architecture.hexagonal.domain.model.aggregate.User;
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
class UserExistsUseCaseTest {

  @InjectMocks
  UserExistsUseCase userExistsUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute_shouldNotThrow_whenUserExists() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    final UserExistsQuery userExistsQuery = UserExistsQueryTestDataBuilder
        .builder()
        .build()
        .userExistsQuery();

    Mockito.when(userRepositoryReadPort.findUserById(userExistsQuery.getUserId()))
        .thenReturn(Optional.of(user));

    userExistsUseCase.execute(userExistsQuery);

    Mockito.verify(userRepositoryReadPort).findUserById(userExistsQuery.getUserId());
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final UserExistsQuery userExistsQuery = UserExistsQueryTestDataBuilder
        .builder()
        .build()
        .userExistsQuery();

    Mockito.when(userRepositoryReadPort.findUserById(userExistsQuery.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
            userExistsUseCase.execute(userExistsQuery))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userExistsQuery.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(userExistsQuery.getUserId());
  }
}
