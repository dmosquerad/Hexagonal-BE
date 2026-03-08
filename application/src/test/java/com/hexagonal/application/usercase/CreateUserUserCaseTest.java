package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.CreateUserUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserDoTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.CreateUserCommandTestDataBuilder;
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
  CreateUserUserCase createUserUserCase;

  @Mock
  UserRepositoryWritePort userRepositoryWritePort;

  @Test
  void execute() {
    final UserDo userDo = UserDoTestDataBuilder
        .builder()
        .build()
        .userDo();

    Mockito.when(userRepositoryWritePort.saveUser(ArgumentMatchers.any(UserDo.class)))
        .thenReturn(userDo);

    UserDo result = createUserUserCase.execute(CreateUserCommandTestDataBuilder
        .builder()
        .build()
        .createUserCommand());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userDo);

    Mockito.verify(userRepositoryWritePort).saveUser(ArgumentMatchers.any(UserDo.class));
  }

}
