package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
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
  void execute_shouldReturnUsers_whenNoFilterIsApplied() {
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

  @Test
  void execute_shouldReturnOnlyUsersMatchingHost_whenHostFilterIsApplied() {
    final User matchingUser = UserTestDataBuilder.builder().build().user();
    final Set<User> allUsers = Collections.singleton(matchingUser);

    final GetAllUserQuery getAllUserQuery = GetAllUserQueryTestDataBuilder
        .builder()
        .host("example")
        .blockEmail(null)
        .build()
        .getAllUserQuery();

    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(allUsers);

    Set<User> result = getAllUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(allUsers);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
    Mockito.verify(emailConfigurationPort, Mockito.never())
        .filterAllowed(ArgumentMatchers.anySet());
  }

  @Test
  void execute_shouldReturnBlockedEmailUsers_whenBlockEmailIsTrue() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Set<User> userSet = Collections.singleton(user);
    final Set<EmailVo> emailVos = Collections.singleton(user.getEmail());

    final GetAllUserQuery getAllUserQuery = GetAllUserQueryTestDataBuilder
        .builder()
        .host("")
        .blockEmail(true)
        .build()
        .getAllUserQuery();

    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(userSet);
    Mockito.when(emailConfigurationPort.filterBlocked(ArgumentMatchers.anySet()))
        .thenReturn(emailVos);

    Set<User> result = getAllUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
    Mockito.verify(emailConfigurationPort).filterBlocked(emailVos);
  }

  @Test
  void execute_shouldReturnAllowedEmailUsers_whenBlockEmailIsFalse() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Set<User> userSet = Collections.singleton(user);
    final Set<EmailVo> emailVos = Collections.singleton(user.getEmail());

    final GetAllUserQuery getAllUserQuery = GetAllUserQueryTestDataBuilder
        .builder()
        .host("")
        .blockEmail(false)
        .build()
        .getAllUserQuery();

    Mockito.when(userRepositoryReadPort.getAllUsers()).thenReturn(userSet);
    Mockito.when(emailConfigurationPort.filterAllowed(ArgumentMatchers.anySet()))
        .thenReturn(emailVos);

    Set<User> result = getAllUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(userSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers();
    Mockito.verify(emailConfigurationPort).filterAllowed(emailVos);
  }

}
