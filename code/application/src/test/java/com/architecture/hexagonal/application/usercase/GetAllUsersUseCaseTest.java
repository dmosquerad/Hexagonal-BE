package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.input.query.GetAllUserQuery;
import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.input.query.GetAllUserQueryTestDataBuilder;
import com.architecture.hexagonal.application.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseTest {

  @InjectMocks
  GetAllUsersUseCase getAllUsersUseCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Mock
  EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute() {
    final Set<User> userSet = Collections.singleton(UserTestDataBuilder
        .builder()
        .build()
        .user());

    final GetAllUserQuery getAllUserQuery = GetAllUserQueryTestDataBuilder
        .builder()
        .build()
        .getAllUserQuery();

    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(userSet);

    Set<User> result = getAllUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
    Mockito.verify(emailConfigurationPort, Mockito.never())
            .filterAllowed(ArgumentMatchers.anySet());
  }

}
