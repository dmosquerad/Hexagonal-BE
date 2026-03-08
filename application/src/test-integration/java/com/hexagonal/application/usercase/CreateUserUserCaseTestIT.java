package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.CreateUserUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.hexagonal.application.testutils.data.entity.UserDoTestDataBuilder;
import com.hexagonal.application.testutils.data.input.command.CreateUserCommandTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.junit.jupiter.api.Test;

@SpringBootTest(classes = CreateUserUserCase.class)
class CreateUserUserCaseTestIT {

  @Autowired
  CreateUserUserCase createUserUserCase;

  @MockitoBean
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
