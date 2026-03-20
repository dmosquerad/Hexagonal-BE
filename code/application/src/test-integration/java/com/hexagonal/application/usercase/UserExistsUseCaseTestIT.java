package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.UserExistsUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.UserExistsQuery;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.query.UserExistsQueryTestDataBuilder;
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
}
