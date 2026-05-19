package com.architecture.hexagonal.application.feature.user.exists.usecase;

import com.architecture.hexagonal.application.testutils.feature.user.exists.query.UserExistsQueryTestDataBuilder;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.application.feature.user.exists.query.UserExistsQuery;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import java.util.Optional;
import org.junit.jupiter.api.Test;
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
}
