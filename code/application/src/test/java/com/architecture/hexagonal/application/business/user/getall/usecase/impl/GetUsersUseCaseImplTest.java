package com.architecture.hexagonal.application.business.user.getall.usecase.impl;

import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.database.query.UserQuery;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.pagination.PaginationResultTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUsersUseCaseImplTest {

  @InjectMocks GetUsersUseCaseImpl getUsersUseCaseImpl;

  @Mock UserRepositoryReadPort userRepositoryReadPort;

  @Mock EmailConfigurationPort emailConfigurationPort;

  @Test
  void
      execute_shouldCallRepositoryGetAllUsersWithFilters_whenPaginationIsNullButBlockedRulesAreProvided() {
    final String host = "example";
    final Boolean blockEmail = false;
    final List<User> users =
        Collections.singletonList(UserTestDataBuilder.builder().build().user());
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    final GetUsersInput query =
        GetUsersInput.builder()
            .host(host)
            .blockEmail(blockEmail)
            .pagination(null)
            .blockedRules(blockedRules)
            .build();

    final UserQuery userQuery =
        UserQuery.builder().host(host).blockEmail(blockEmail).blockedRules(blockedRules).build();

    final PaginationResult<User> expectedResult =
        PaginationResultTestDataBuilder.<User>builder().data(users).build().paginationResult();

    Mockito.when(userRepositoryReadPort.getAllUsers(userQuery)).thenReturn(expectedResult);

    PaginationResult<User> result = getUsersUseCaseImpl.execute(query);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);

    Mockito.verify(userRepositoryReadPort).getAllUsers(userQuery);
    Mockito.verify(emailConfigurationPort, Mockito.never()).getBlockedRules();
  }

  @Test
  void execute_shouldReturnBlockedEmailUsers_whenBlockEmailIsTrue() {
    final User user = UserTestDataBuilder.builder().build().user();
    final List<User> users = Collections.singletonList(user);

    final String host = "";
    final Boolean blockEmail = true;
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final Set<String> blockedHosts = Set.of("example");
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().host(blockedHosts).build().emailBlockRulesVo();

    final GetUsersInput query =
        GetUsersInput.builder()
            .host(host)
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .pagination(pagination)
            .build();

    final UserQuery userQuery =
        UserQuery.builder().host(host).blockEmail(blockEmail).blockedRules(blockedRules).build();

    Mockito.when(userRepositoryReadPort.getAllUsers(userQuery, pagination))
        .thenReturn(
            PaginationResultTestDataBuilder.<User>builder().data(users).build().paginationResult());

    PaginationResult<User> result = getUsersUseCaseImpl.execute(query);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .isEqualTo(users);

    Mockito.verify(userRepositoryReadPort).getAllUsers(userQuery, pagination);
    Mockito.verifyNoInteractions(emailConfigurationPort);
  }

  @Test
  void execute_shouldReturnAllowedEmailUsers_whenBlockEmailIsFalse() {
    final User user = UserTestDataBuilder.builder().build().user();
    final List<User> users = Collections.singletonList(user);

    final String host = "";
    final Boolean blockEmail = false;
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    final GetUsersInput query =
        GetUsersInput.builder()
            .host(host)
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .pagination(pagination)
            .build();

    final UserQuery userQuery =
        UserQuery.builder().host(host).blockEmail(blockEmail).blockedRules(blockedRules).build();

    Mockito.when(userRepositoryReadPort.getAllUsers(userQuery, pagination))
        .thenReturn(
            PaginationResultTestDataBuilder.<User>builder().data(users).build().paginationResult());

    PaginationResult<User> result = getUsersUseCaseImpl.execute(query);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .isEqualTo(users);

    Mockito.verify(userRepositoryReadPort).getAllUsers(userQuery, pagination);
    Mockito.verifyNoInteractions(emailConfigurationPort);
  }
}
