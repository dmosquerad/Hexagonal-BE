package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.CreateUserUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.stub.UserDoStub;
import org.junit.jupiter.api.Assertions;
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
  CreateUserUserCase createUserUserCase;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void testExecuteCreatesUser() {
    final CreateUserCommand createUserCommand = CreateUserCommand.builder()
        .email(UserDoStub.USERDO.getEmail())
        .name(UserDoStub.USERDO.getName())
        .build();

    Mockito.when(userRepositoryWritePort.createUser(ArgumentMatchers.any(UserDo.class))).thenReturn(
        UserDoStub.USERDO);

    UserDo result = createUserUserCase.execute(createUserCommand);

    Assertions.assertEquals(UserDoStub.USERDO, result);

    Mockito.verify(userRepositoryWritePort).createUser(ArgumentMatchers.any(UserDo.class));
  }

}
