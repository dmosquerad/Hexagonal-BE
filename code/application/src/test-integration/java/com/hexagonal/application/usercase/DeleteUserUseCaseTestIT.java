package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.DeleteUserCommandTestDataBuilder;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = DeleteUserUseCase.class)
class DeleteUserUseCaseTestIT {

  @Autowired
  DeleteUserUseCase deleteUserUseCase;

  @MockitoBean
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void execute() throws ResourceNotFoundException {
    final User user = UserTestDataBuilder
        .builder()
        .build()
        .user();
    final DeleteUserCommand deleteUserCommand = DeleteUserCommandTestDataBuilder
        .builder()
        .userId(user.getUserId())
        .build()
        .deleteUserCommand();

    Mockito.when(userRepositoryWritePort.deleteUser(deleteUserCommand.getUserId()))
        .thenReturn(Optional.of(user));

    final User result = deleteUserUseCase.execute(deleteUserCommand);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(user);

    Mockito.verify(userRepositoryWritePort).deleteUser(deleteUserCommand.getUserId());
  }
}
