package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.CreateUserUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.CreateUserCommandTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.junit.jupiter.api.Test;

@SpringBootTest(classes = CreateUserUseCase.class)
class CreateUserUserCaseTestIT {

  @Autowired
  CreateUserUseCase createUserUseCase;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void execute() {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    User result = createUserUseCase.execute(CreateUserCommandTestDataBuilder
        .builder()
        .build()
        .createUserCommand());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
  }

}
