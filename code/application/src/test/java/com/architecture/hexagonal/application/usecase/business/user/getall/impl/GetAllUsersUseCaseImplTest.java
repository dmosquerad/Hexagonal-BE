package com.architecture.hexagonal.application.usecase.business.user.getall.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.projector.business.user.UserEmailProjector;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.application.testutils.model.aggregate.pagination.PaginationResultVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.model.aggregate.pagination.PaginationVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.orchestration.data.query.GetUsersInputTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.contract.application.generated.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseImplTest {

  @Mock private UserService userService;

  @Mock private EmailService emailService;

  @Spy private TransactionReadBoundary transactionBoundary = new SpyTransactionReadBoundary();

  @InjectMocks private GetAllUsersUseCaseImpl useCase;

  @Test
  void handleShouldNotFetchBlockedRulesWhenBlockEmailIsNull() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().build().getUsersInput();
    final UserEmailProjector projector =
        UserEmailProjector.builder()
            .host(query.getHost())
            .blockEmail(query.getBlockEmail())
            .build();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.getPagination().getPage())
            .size(query.getPagination().getSize())
            .build()
            .pagination();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(userService.getAllUsers(projector, paginationVo)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(emailService, Mockito.never()).getBlockedRules();
    Mockito.verify(userService).getAllUsers(projector, paginationVo);
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }

  @Test
  void handleShouldUseUnpaginatedServiceOverloadWhenPaginationIsNotProvided() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().build().getUsersInput();
    query.setPagination(null);
    final UserEmailProjector projector =
        UserEmailProjector.builder()
            .host(query.getHost())
            .blockEmail(query.getBlockEmail())
            .build();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(userService.getAllUsers(projector)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(userService).getAllUsers(projector);
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }

  @Test
  void handleShouldFetchBlockedRulesWhenBlockEmailIsProvided() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().blockEmail(true).build().getUsersInput();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    final UserEmailProjector projector =
        UserEmailProjector.builder()
            .host(query.getHost())
            .blockEmail(query.getBlockEmail())
            .blockedRules(blockedRules)
            .build();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.getPagination().getPage())
            .size(query.getPagination().getSize())
            .build()
            .pagination();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(emailService.getBlockedRules()).thenReturn(blockedRules);
    Mockito.when(userService.getAllUsers(projector, paginationVo)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(userService).getAllUsers(projector, paginationVo);
    Mockito.verify(transactionBoundary, Mockito.times(2)).execute(Mockito.any());
  }

  @Test
  void handleShouldPreserveFalseWhenBlockEmailIsFalse() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().blockEmail(false).build().getUsersInput();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    final UserEmailProjector projector =
        UserEmailProjector.builder()
            .host(query.getHost())
            .blockEmail(query.getBlockEmail())
            .blockedRules(blockedRules)
            .build();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.getPagination().getPage())
            .size(query.getPagination().getSize())
            .build()
            .pagination();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(emailService.getBlockedRules()).thenReturn(blockedRules);
    Mockito.when(userService.getAllUsers(projector, paginationVo)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(userService).getAllUsers(projector, paginationVo);
    Mockito.verify(transactionBoundary, Mockito.times(2)).execute(Mockito.any());
  }
}
