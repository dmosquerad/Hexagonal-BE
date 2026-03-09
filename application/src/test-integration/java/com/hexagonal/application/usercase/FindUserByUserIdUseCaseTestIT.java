package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.query.FindUserByUserIdQueryTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
  void execute() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(user);

    User result = findUserByUserIdUseCase.execute(FindUserByUserIdQueryTestDataBuilder
        .builder()
        .build()
        .findUserByUserIdQuery());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
  }

}
