package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseTest {

  @InjectMocks
  GetAllUsersUseCase getAllUsersUseCase;

  @Mock
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
