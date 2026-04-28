package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.cqrs.command.request.CreateUserCommand;
import com.architecture.hexagonal.application.port.out.UserSenderPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.command.CreateUserCommandTestDataBuilder;
import com.architecture.hexagonal.application.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.model.entity.User;
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

  @MockitoBean
  UserSenderPort eventPublisherPort;

  @Test
  void execute_shouldCreateUser_whenCommandIsValid() {
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
    Mockito.verify(eventPublisherPort).userSenderCreated(ArgumentMatchers.any(User.class));
  }

}
