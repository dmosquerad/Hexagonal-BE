package com.architecture.hexagonal.application.business.user.getall.usecase.impl;

import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.testutils.data.aggregate.pagination.PaginationResultTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.aggregate.pagination.PaginationTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.application.testutils.data.vo.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
import com.architecture.hexagonal.domain.model.aggregate.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.domain.model.projector.user.UserEmailProjector;
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
class GetAllUsersUseCaseImplTest {

  @InjectMocks GetAllUsersUseCaseImpl getAllUsersUseCaseImpl;

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

    final UserEmailProjector userEmailProjector =
        UserEmailProjector.builder()
            .host(host)
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .build();

    final PaginationResult<User> expectedResult =
        PaginationResultTestDataBuilder.<User>builder().data(users).build().paginationResult();

    Mockito.when(userRepositoryReadPort.getAllUsers(userEmailProjector)).thenReturn(expectedResult);

    PaginationResult<User> result = getAllUsersUseCaseImpl.execute(query);

    AssertionsForClassTypes.assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);

    Mockito.verify(userRepositoryReadPort).getAllUsers(userEmailProjector);
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

    final UserEmailProjector userEmailProjector =
        UserEmailProjector.builder()
            .host(host)
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .build();

    Mockito.when(userRepositoryReadPort.getAllUsers(userEmailProjector, pagination))
        .thenReturn(
            PaginationResultTestDataBuilder.<User>builder().data(users).build().paginationResult());

    PaginationResult<User> result = getAllUsersUseCaseImpl.execute(query);

    AssertionsForClassTypes.assertThat(result.data()).usingRecursiveComparison().isEqualTo(users);

    Mockito.verify(userRepositoryReadPort).getAllUsers(userEmailProjector, pagination);
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

    final UserEmailProjector userEmailProjector =
        UserEmailProjector.builder()
            .host(host)
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .build();

    Mockito.when(userRepositoryReadPort.getAllUsers(userEmailProjector, pagination))
        .thenReturn(
            PaginationResultTestDataBuilder.<User>builder().data(users).build().paginationResult());

    PaginationResult<User> result = getAllUsersUseCaseImpl.execute(query);

    AssertionsForClassTypes.assertThat(result.data()).usingRecursiveComparison().isEqualTo(users);

    Mockito.verify(userRepositoryReadPort).getAllUsers(userEmailProjector, pagination);
    Mockito.verifyNoInteractions(emailConfigurationPort);
  }
}
