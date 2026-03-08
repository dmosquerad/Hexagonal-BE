package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.GetAllUsersUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserDoTestDataBuilder;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = GetAllUsersUserCase.class)
class GetAllUsersUserCaseTestIT {

  @Autowired
  GetAllUsersUserCase getAllUsersUserCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute() {
    final Set<UserDo> userDoSet = Collections.singleton(UserDoTestDataBuilder
        .builder()
        .build()
        .userDo());

    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(userDoSet);

    Set<UserDo> result = getAllUsersUserCase.execute();

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userDoSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
  }

}
