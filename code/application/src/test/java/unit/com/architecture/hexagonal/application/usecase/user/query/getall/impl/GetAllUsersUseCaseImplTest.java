package com.architecture.hexagonal.application.usecase.user.query.getall.impl;

import com.architecture.hexagonal.application.databuilder.model.query.user.GetUsersInputTestDataBuilder;
import com.architecture.hexagonal.application.databuilder.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.model.query.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.domain.databuilder.model.criteria.user.UserEmailCriteriaTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.pagination.PaginationResultVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.pagination.PaginationVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.criteria.user.UserEmailCriteria;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionReadBoundary;
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
    final UserEmailCriteria criteria =
        UserEmailCriteriaTestDataBuilder.builder()
            .blockEmail(null)
            .blockedRules(null)
            .build()
            .userEmailCriteria();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.pagination().page())
            .size(query.pagination().size())
            .build()
            .paginationVo();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(userService.getAllUsers(criteria, paginationVo)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(emailService, Mockito.never()).getBlockedRules();
    Mockito.verify(userService).getAllUsers(criteria, paginationVo);
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }

  @Test
  void handleShouldUseUnpaginatedServiceOverloadWhenPaginationIsNotProvided() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().pagination(null).build().getUsersInput();
    final UserEmailCriteria criteria =
        UserEmailCriteriaTestDataBuilder.builder()
            .blockEmail(null)
            .blockedRules(null)
            .build()
            .userEmailCriteria();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(userService.getAllUsers(criteria)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(userService).getAllUsers(criteria);
    Mockito.verify(transactionBoundary).execute(Mockito.any());
  }

  @Test
  void handleShouldFetchBlockedRulesWhenBlockEmailIsProvided() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().blockEmail(true).build().getUsersInput();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    final UserEmailCriteria criteria =
        UserEmailCriteriaTestDataBuilder.builder()
            .blockEmail(query.blockEmail())
            .build()
            .userEmailCriteria();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.pagination().page())
            .size(query.pagination().size())
            .build()
            .paginationVo();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(emailService.getBlockedRules()).thenReturn(blockedRules);
    Mockito.when(userService.getAllUsers(criteria, paginationVo)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(userService).getAllUsers(criteria, paginationVo);
    Mockito.verify(transactionBoundary, Mockito.times(2)).execute(Mockito.any());
  }

  @Test
  void handleShouldPreserveFalseWhenBlockEmailIsFalse() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder().blockEmail(false).build().getUsersInput();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    final UserEmailCriteria criteria =
        UserEmailCriteriaTestDataBuilder.builder()
            .blockEmail(query.blockEmail())
            .build()
            .userEmailCriteria();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.pagination().page())
            .size(query.pagination().size())
            .build()
            .paginationVo();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder().build().paginationResult();
    Mockito.when(emailService.getBlockedRules()).thenReturn(blockedRules);
    Mockito.when(userService.getAllUsers(criteria, paginationVo)).thenReturn(expectedResult);

    final PaginationResultVo<User> result = useCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(userService).getAllUsers(criteria, paginationVo);
    Mockito.verify(transactionBoundary, Mockito.times(2)).execute(Mockito.any());
  }
}
