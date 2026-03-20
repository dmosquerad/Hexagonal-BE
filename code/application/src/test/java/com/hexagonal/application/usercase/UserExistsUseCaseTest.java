package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.UserExistsUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.UserExistsQuery;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.query.UserExistsQueryTestDataBuilder;
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
  void execute() throws ResourceNotFoundException {
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
  void executeUserNotFound() {
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
