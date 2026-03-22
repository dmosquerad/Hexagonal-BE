package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = GetAllUsersUseCase.class)
class GetAllUsersUseCaseTestIT {

  @Autowired
  GetAllUsersUseCase getAllUsersUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute() {
    final Set<User> userSet = Collections.singleton(UserTestDataBuilder
        .builder()
        .build()
        .user());

    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(userSet);

    Set<User> result = getAllUsersUseCase.execute();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
  }

}
