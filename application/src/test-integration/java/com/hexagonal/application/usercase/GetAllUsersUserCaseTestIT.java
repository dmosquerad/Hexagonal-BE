package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.GetAllUsersUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.stub.UserDoStub;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
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
  void testExecuteReturnsAllUsers() {
    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(UserDoStub.USERDO_SET);

    Set<UserDo> result = getAllUsersUserCase.execute();

    Assertions.assertEquals(UserDoStub.USERDO_SET, result);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
  }

}
