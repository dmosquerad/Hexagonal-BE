package com.architecture.hexagonal.application.business.user.findbyid.usecase.impl;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.user.findbyid.input.FindUserByUserIdQueryTestDataBuilder;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
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
    final FindUserByUserIdQuery findUserByUserIdQuery =
        FindUserByUserIdQueryTestDataBuilder.builder().build().findUserByUserIdQuery();

    Mockito.when(userRepositoryReadPort.findUserById(findUserByUserIdQuery.getUserId()))
        .thenReturn(Optional.of(user));

    User result = findUserByUserIdUseCaseImpl.execute(findUserByUserIdQuery);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(findUserByUserIdQuery.getUserId());
  }

  @Test
  void execute_shouldThrowResourceNotFoundException_whenUserNotFound() {
    final FindUserByUserIdQuery findUserByUserIdQuery =
        FindUserByUserIdQueryTestDataBuilder.builder().build().findUserByUserIdQuery();

    Mockito.when(userRepositoryReadPort.findUserById(findUserByUserIdQuery.getUserId()))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(
            () -> findUserByUserIdUseCaseImpl.execute(findUserByUserIdQuery))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + findUserByUserIdQuery.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(findUserByUserIdQuery.getUserId());
  }
}
