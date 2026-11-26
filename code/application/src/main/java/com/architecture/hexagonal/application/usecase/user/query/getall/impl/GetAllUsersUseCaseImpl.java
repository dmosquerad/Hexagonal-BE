package com.architecture.hexagonal.application.usecase.user.query.getall.impl;

import com.architecture.hexagonal.application.mapper.pagination.PaginationVoMapper;
import com.architecture.hexagonal.application.model.query.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.application.model.vo.pagination.Pagination;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.domain.annotation.ApplicationUseCase;
import com.architecture.hexagonal.domain.model.criteria.user.UserEmailCriteria;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionReadBoundary;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class GetAllUsersUseCaseImpl
    implements QueryHandler<GetUsersFilteredQuery, PaginationResultVo<User>> {

  private final TransactionReadBoundary transactionReadBoundary;
  private final EmailService emailService;
  private final UserService userService;

  @Override
  public PaginationResultVo<User> handle(
      final @NonNull GetUsersFilteredQuery getUsersFilteredQuery) {
    final Boolean blockEmail = getUsersFilteredQuery.blockEmail();
    final Pagination contractPagination = getUsersFilteredQuery.pagination();
    final PaginationVo paginationVo =
        Objects.isNull(contractPagination)
            ? null
            : PaginationVoMapper.INSTANCE.toPaginationVo(contractPagination);
    final EmailBlockRulesVo blockedRules =
        Objects.nonNull(blockEmail)
            ? transactionReadBoundary.execute(emailService::getBlockedRules)
            : null;
    final UserEmailCriteria criteria =
        UserEmailCriteria.builder()
            .host(getUsersFilteredQuery.host())
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .build();

    return transactionReadBoundary.execute(
        () ->
            Objects.isNull(paginationVo)
                ? userService.getAllUsers(criteria)
                : userService.getAllUsers(criteria, paginationVo));
  }
}
