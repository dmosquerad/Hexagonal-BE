package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.feature.user.findbyid.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.query.FindUserByUserIdQueryTestDataBuilder;
import com.architecture.hexagonal.application.feature.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = FindUserByUserIdUseCase.class)
class FindUserByUserIdUseCaseTestIT {

  @Autowired
  FindUserByUserIdUseCase findUserByUserIdUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute_shouldReturnUser_whenUserExists() throws ResourceNotFoundException {
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
}
