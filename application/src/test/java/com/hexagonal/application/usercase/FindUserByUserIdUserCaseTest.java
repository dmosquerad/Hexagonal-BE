package com.hexagonal.application.usercase;

import com.architecture.hexagonal.application.usercase.FindUserByUserIdUserCase;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.hexagonal.application.testutils.data.entity.UserDoTestDataBuilder;
import com.hexagonal.application.testutils.data.input.query.FindUserByUserIdQueryTestDataBuilder;
import java.util.UUID;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindUserByUserIdUserCaseTest {

  @InjectMocks
  FindUserByUserIdUserCase findUserByUserIdUserCase;

  @Mock
  UserRepositoryReadPort userRepositoryReadPort;

  @Test
  void execute() {
    final UserDo userDo = UserDoTestDataBuilder
        .builder()
        .build()
        .userDo();

    Mockito.when(userRepositoryReadPort.findUserById(ArgumentMatchers.any(UUID.class)))
        .thenReturn(userDo);

    UserDo result = findUserByUserIdUserCase.execute(FindUserByUserIdQueryTestDataBuilder
        .builder()
        .build()
        .findUserByUserIdQuery());

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userDo);

    Mockito.verify(userRepositoryReadPort).findUserById(ArgumentMatchers.any(UUID.class));
  }

}
