package com.architecture.hexagonal.application.feature.user.getall.usecase;

import com.architecture.hexagonal.application.feature.user.getall.query.GetUsersQuery;
import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.common.pagination.PaginationResultTestDataBuilder;
import com.architecture.hexagonal.application.testutils.common.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.feature.user.getall.query.GetUsersQueryTestDataBuilder;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Collections;
import java.util.Set;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {GetUsersUseCase.class} )
class GetUsersUseCaseTestIT {

  @Autowired
  GetUsersUseCase getUsersUseCase;

  @MockitoBean
  UserRepositoryReadPort userRepositoryReadPort;

  @MockitoBean
  EmailConfigurationPort emailConfigurationPort;

  @Test
  void execute_shouldReturnUsers_whenNoFilterIsApplied() {
    final String host = "";
    final Boolean blockEmail = null;
    final Set<User> userSet = Collections.singleton(UserTestDataBuilder
        .builder()
        .build()
        .user());

    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();
    final GetUsersQuery getAllUserQuery = GetUsersQueryTestDataBuilder
        .builder()
        .host(host)
        .blockEmail(blockEmail)
        .pagination(pagination)
        .build()
        .getUsersQuery();

    Mockito.when(userRepositoryReadPort.getAllUsers(host, pagination))
        .thenReturn(PaginationResultTestDataBuilder.<User>builder()
            .data(userSet)
            .build()
            .paginationResult());

    PaginationResult<User> result = getUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .isEqualTo(userSet);
    Mockito.verify(userRepositoryReadPort).getAllUsers(host, pagination);
    Mockito.verify(emailConfigurationPort, Mockito.never()).getBlockedRules();
  }

  @Test
  void execute_shouldReturnOnlyUsersMatchingHost_whenHostFilterIsApplied() {
    final String host = "example";
    final Boolean blockEmail = null;
    final User matchingUser = UserTestDataBuilder.builder().build().user();
    final Set<User> allUsers = Collections.singleton(matchingUser);

    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();
    final GetUsersQuery getAllUserQuery = GetUsersQueryTestDataBuilder
        .builder()
        .host(host)
        .blockEmail(blockEmail)
        .pagination(pagination)
        .build()
        .getUsersQuery();

    Mockito.when(userRepositoryReadPort.getAllUsers(host, pagination))
        .thenReturn(PaginationResultTestDataBuilder.<User>builder()
            .data(allUsers)
            .build()
            .paginationResult());

    PaginationResult<User> result = getUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .isEqualTo(allUsers);

    Mockito.verify(userRepositoryReadPort).getAllUsers(host, pagination);
    Mockito.verify(emailConfigurationPort, Mockito.never()).getBlockedRules();
  }

  @Test
  void execute_shouldReturnBlockedEmailUsers_whenBlockEmailIsTrue() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Set<User> userSet = Collections.singleton(user);

    final String host = "";
    final Boolean blockEmail = true;
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final Set<String> blockedHosts = Set.of("example");
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVoTestDataBuilder.builder()
        .host(blockedHosts)
        .build()
        .emailBlockRulesVo();
    final GetUsersQuery getUsersQuery = GetUsersQuery.builder()
        .host(host)
        .blockEmail(blockEmail)
        .blockedRules(blockedRules)
        .pagination(pagination)
        .build();

    Mockito.when(userRepositoryReadPort.getAllUsers(host, blockEmail,
            blockedRules,
            pagination))
        .thenReturn(PaginationResultTestDataBuilder.<User>builder()
            .data(userSet)
            .build()
            .paginationResult());

    PaginationResult<User> result = getUsersUseCase.execute(getUsersQuery);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .isEqualTo(userSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers(host, blockEmail, blockedRules, pagination);
    Mockito.verifyNoInteractions(emailConfigurationPort);
  }

  @Test
  void execute_shouldReturnAllowedEmailUsers_whenBlockEmailIsFalse() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Set<User> userSet = Collections.singleton(user);

    final String host = "";
    final Boolean blockEmail = false;
    final Pagination pagination = PaginationTestDataBuilder.builder().build().pagination();

    final EmailBlockRulesVo blockedRules = EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    final GetUsersQuery getUsersQuery = GetUsersQuery.builder()
        .host(host)
        .blockEmail(blockEmail)
        .blockedRules(blockedRules)
        .pagination(pagination)
        .build();

    Mockito.when(userRepositoryReadPort.getAllUsers(host, blockEmail,
            blockedRules,
            pagination))
        .thenReturn(PaginationResultTestDataBuilder.<User>builder()
            .data(userSet)
            .build()
            .paginationResult());

    PaginationResult<User> result = getUsersUseCase.execute(getUsersQuery);

    AssertionsForClassTypes.assertThat(result.getData())
        .usingRecursiveComparison()
        .isEqualTo(userSet);

    Mockito.verify(userRepositoryReadPort).getAllUsers(host, blockEmail, blockedRules, pagination);
    Mockito.verifyNoInteractions(emailConfigurationPort);
  }

  @Test
  void execute_shouldRespectPaginationParameters_whenCustomPageAndSizeAreProvided() {
    final String host = "";
    final Boolean blockEmail = null;
    final Set<User> userSet = Collections.singleton(UserTestDataBuilder.builder().build().user());
    final Pagination pagination = PaginationTestDataBuilder.builder().page(2).size(5).build().pagination();
    final GetUsersQuery getAllUserQuery = GetUsersQueryTestDataBuilder
        .builder()
        .host(host)
        .blockEmail(blockEmail)
        .pagination(pagination)
        .build()
        .getUsersQuery();

    final PaginationResult<User> expectedResult = PaginationResultTestDataBuilder.<User>builder()
        .data(userSet)
        .totalElements(11L)
        .totalPages(3)
        .page(2)
        .size(5)
        .build()
        .paginationResult();

    Mockito.when(userRepositoryReadPort.getAllUsers(host, pagination))
        .thenReturn(expectedResult);

    PaginationResult<User> result = getUsersUseCase.execute(getAllUserQuery);

    AssertionsForClassTypes.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expectedResult);

    Mockito.verify(userRepositoryReadPort).getAllUsers(host, pagination);
    Mockito.verify(emailConfigurationPort, Mockito.never()).getBlockedRules();
  }

}
