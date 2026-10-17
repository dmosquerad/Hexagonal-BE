package com.architecture.hexagonal.application.usecase.user.getall.impl;

import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.projector.user.UserEmailProjector;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.testutils.projector.user.UserEmailProjectorTestDataBuilder;
import com.architecture.hexagonal.application.testutils.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.testutils.usecase.query.GetUsersInputTestDataBuilder;
import com.architecture.hexagonal.contract.application.usecase.generated.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.pagination.PaginationResultVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.model.vo.pagination.PaginationVoTestDataBuilder;
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
        UserEmailProjectorTestDataBuilder.builder()
            .blockEmail(null)
            .blockedRules(null)
            .build()
            .userEmailProjector();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.getPagination().getPage())
            .size(query.getPagination().getSize())
            .build()
            .paginationVo();
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
        UserEmailProjectorTestDataBuilder.builder()
            .blockEmail(null)
            .blockedRules(null)
            .build()
            .userEmailProjector();
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
        UserEmailProjectorTestDataBuilder.builder()
            .blockEmail(query.getBlockEmail())
            .build()
            .userEmailProjector();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.getPagination().getPage())
            .size(query.getPagination().getSize())
            .build()
            .paginationVo();
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
        UserEmailProjectorTestDataBuilder.builder()
            .blockEmail(query.getBlockEmail())
            .build()
            .userEmailProjector();
    final PaginationVo paginationVo =
        PaginationVoTestDataBuilder.builder()
            .page(query.getPagination().getPage())
            .size(query.getPagination().getSize())
            .build()
            .paginationVo();
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
