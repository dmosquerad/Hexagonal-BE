package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.query.FindUserByUserIdQueryTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
