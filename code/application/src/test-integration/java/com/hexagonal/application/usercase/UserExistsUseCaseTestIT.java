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
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UserExistsUseCase.class)
class UserExistsUseCaseTestIT {

  @Autowired
  UserExistsUseCase userExistsUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(user));

    final UserExistsQuery userExistsQuery = UserExistsQueryTestDataBuilder
        .builder()
        .build()
        .userExistsQuery();

    userExistsUseCase.execute(userExistsQuery);

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
  }

  @Test
  void executeUserNotFound() {
    final UserExistsQuery userExistsQuery = UserExistsQueryTestDataBuilder
        .builder()
        .build()
        .userExistsQuery();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() ->
            userExistsUseCase.execute(userExistsQuery))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + userExistsQuery.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
  }
}
