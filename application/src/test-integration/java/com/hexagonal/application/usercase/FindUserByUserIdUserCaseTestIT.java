package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.FindUserByUserIdUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.stub.UserDoStub;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = FindUserByUserIdUserCase.class)
class FindUserByUserIdUserCaseTestIT {

  @Autowired
  FindUserByUserIdUserCase findUserByUserIdUserCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void testExecuteFindUserById() {
    final FindUserByUserIdQuery findUserByUserIdQuery = FindUserByUserIdQuery.builder()
        .userId(UserDoStub.USERDO.getUserId())
        .build();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(UserDoStub.USERDO);

    UserDo result = findUserByUserIdUserCase.execute(findUserByUserIdQuery);

    Assertions.assertEquals(UserDoStub.USERDO, result);

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
  }

}
