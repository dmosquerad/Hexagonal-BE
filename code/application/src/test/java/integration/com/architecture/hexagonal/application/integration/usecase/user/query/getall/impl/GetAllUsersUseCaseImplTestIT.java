package com.architecture.hexagonal.application.integration.usecase.user.query.getall.impl;

import com.architecture.hexagonal.application.databuilder.transaction.SpyTransactionReadBoundary;
import com.architecture.hexagonal.application.databuilder.model.query.user.GetUsersInputTestDataBuilder;
import com.architecture.hexagonal.application.usecase.user.query.getall.impl.GetAllUsersUseCaseImpl;
import com.architecture.hexagonal.domain.port.out.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.domain.port.out.repository.user.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.criteria.user.UserEmailCriteria;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.email.impl.EmailServiceImpl;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.application.service.user.impl.UserServiceImpl;
import com.architecture.hexagonal.domain.databuilder.model.vo.pagination.PaginationResultVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.model.query.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.assertj.core.api.AssertionsForClassTypes;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseImplTestIT {

  @Mock private UserRepositoryReadPort userRepositoryReadPort;
  @Mock private EmailConfigurationPort emailConfigurationPort;
  @Mock private EmailService emailService;
  @Mock private UserService userService;

  @Spy private SpyTransactionReadBoundary transactionReadBoundary =
      new SpyTransactionReadBoundary();

  @InjectMocks private EmailServiceImpl emailServiceImpl;
  @InjectMocks private UserServiceImpl userServiceImpl;
  @InjectMocks private GetAllUsersUseCaseImpl getAllUsersUseCase;

  @Test
  void getUsersFilteredUseCaseShouldLoadRulesAndReturnUsersWhenBlockEmailIsProvided() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder()
            .blockEmail(true)
            .build()
            .getUsersInput();
    final EmailBlockRulesVo blockedRules =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder()
            .page(1)
            .size(10)
            .totalElements(0L)
            .totalPages(0)
            .build()
            .paginationResult();

    Mockito.when(emailConfigurationPort.getBlockedRules()).thenReturn(blockedRules);
    Mockito.when(emailService.getBlockedRules())
        .thenAnswer(invocation -> emailServiceImpl.getBlockedRules());
    Mockito.when(
            userService.getAllUsers(
                ArgumentMatchers.any(UserEmailCriteria.class),
                ArgumentMatchers.any(PaginationVo.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (UserEmailCriteria criteria, PaginationVo pagination) ->
                    userServiceImpl.getAllUsers(criteria, pagination)));
    Mockito.when(
            userRepositoryReadPort.getAllUsers(
                ArgumentMatchers.any(UserEmailCriteria.class),
                ArgumentMatchers.any(PaginationVo.class)))
        .thenReturn(expectedResult);

    final PaginationResultVo<User> result = getAllUsersUseCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(transactionReadBoundary, Mockito.times(2)).execute(ArgumentMatchers.any());
    Mockito.verify(emailConfigurationPort).getBlockedRules();
    Mockito.verify(emailService).getBlockedRules();
    Mockito.verify(userService)
        .getAllUsers(
            ArgumentMatchers.any(UserEmailCriteria.class),
            ArgumentMatchers.any(PaginationVo.class));
    Mockito.verify(userRepositoryReadPort)
        .getAllUsers(
            ArgumentMatchers.any(UserEmailCriteria.class),
            ArgumentMatchers.any(PaginationVo.class));
  }

  @Test
  void getUsersFilteredUseCaseShouldSkipRulesWhenBlockEmailIsNotProvided() {
    final GetUsersFilteredQuery query =
        GetUsersInputTestDataBuilder.builder()
            .pagination(null)
            .build()
            .getUsersInput();
    final PaginationResultVo<User> expectedResult =
        PaginationResultVoTestDataBuilder.<User>builder()
            .size(0)
            .totalElements(0L)
            .build()
            .paginationResult();

    Mockito.when(userService.getAllUsers(ArgumentMatchers.any(UserEmailCriteria.class)))
        .thenAnswer(
            AdditionalAnswers.answer(
                (UserEmailCriteria criteria) -> userServiceImpl.getAllUsers(criteria)));
    Mockito.when(userRepositoryReadPort.getAllUsers(ArgumentMatchers.any(UserEmailCriteria.class)))
        .thenReturn(expectedResult);

    final PaginationResultVo<User> result = getAllUsersUseCase.handle(query);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);
    Mockito.verify(transactionReadBoundary).execute(ArgumentMatchers.any());
    Mockito.verify(emailConfigurationPort, Mockito.never()).getBlockedRules();
    Mockito.verify(emailService, Mockito.never()).getBlockedRules();
    Mockito.verify(userService).getAllUsers(ArgumentMatchers.any(UserEmailCriteria.class));
    Mockito.verify(userRepositoryReadPort)
        .getAllUsers(ArgumentMatchers.any(UserEmailCriteria.class));
  }
}
