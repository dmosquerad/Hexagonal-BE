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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUserCaseTest {

  @InjectMocks
  CreateUserUseCase createUserUseCase;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Mock
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
    Mockito.verify(eventPublisherPort).userSenderCreated(user);
  }

}
