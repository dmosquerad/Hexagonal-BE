package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.query.FindUserByUserIdQueryTestDataBuilder;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindUserByUserIdUseCaseTest {

  @InjectMocks
  FindUserByUserIdUseCase findUserByUserIdUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final FindUserByUserIdQuery findUserByUserIdQuery = FindUserByUserIdQueryTestDataBuilder
        .builder()
        .build()
        .findUserByUserIdQuery();

    Mockito.when(userRepositoryReadPort.findUserById(findUserByUserIdQuery.getUserId()))
        .thenReturn(Optional.of(user));

    User result = findUserByUserIdUseCase.execute(findUserByUserIdQuery);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(findUserByUserIdQuery.getUserId());
  }

  @Test
  void executeUserNotFound() {
    final FindUserByUserIdQuery findUserByUserIdQuery = FindUserByUserIdQueryTestDataBuilder
        .builder()
        .build()
        .findUserByUserIdQuery();

    Mockito.when(userRepositoryReadPort.findUserById(findUserByUserIdQuery.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
            findUserByUserIdUseCase.execute(findUserByUserIdQuery))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + findUserByUserIdQuery.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(findUserByUserIdQuery.getUserId());
  }
}
