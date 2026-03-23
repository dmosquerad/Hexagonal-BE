package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.command.CreateUserCommandTestDataBuilder;
import com.architecture.hexagonal.application.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    final CreateUserCommand createUserCommand = CreateUserCommandTestDataBuilder
      .builder()
      .build()
      .createUserCommand();

    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    User result = createUserUseCase.execute(createUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
  }

}
