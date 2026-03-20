package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.UpdateUserCommandTestDataBuilder;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = UpdateUserUseCase.class)
class UpdateUserUseCaseTestIT {

  @Autowired
  UpdateUserUseCase updateUserUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void execute() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.of(user));
    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(User.class)))
        .thenReturn(user);

    final User result = updateUserUseCase.execute(UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(User.class));
  }

  @Test
  void executeUserNotFound() {
    final UpdateUserCommand updateUserCommand = UpdateUserCommandTestDataBuilder
        .builder()
        .build()
        .updateUserCommand();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(Optional.empty());

    AssertionsForClassTypes.assertThatThrownBy(() -> updateUserUseCase.execute(updateUserCommand))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + updateUserCommand.getUserId());

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
    Mockito.verify(userRepositoryWritePort, Mockito.never()).saveUser(ArgumentMatchers.any(User.class));
  }

}