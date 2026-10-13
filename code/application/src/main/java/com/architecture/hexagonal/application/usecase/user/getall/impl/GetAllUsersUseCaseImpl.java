package com.architecture.hexagonal.application.usecase.user.getall.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.projector.user.UserEmailProjector;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.contract.application.usecase.generated.common.Pagination;
import com.architecture.hexagonal.contract.application.usecase.generated.user.GetUsersFilteredQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
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
    final Boolean blockEmail = getUsersFilteredQuery.getBlockEmail();
    final Pagination contractPagination = getUsersFilteredQuery.getPagination();
    final PaginationVo paginationVo =
        Objects.isNull(contractPagination)
            ? null
            : PaginationVo.builder()
                .page(contractPagination.getPage())
                .size(contractPagination.getSize())
                .build();
    final EmailBlockRulesVo blockedRules =
        Objects.nonNull(blockEmail)
            ? transactionReadBoundary.execute(emailService::getBlockedRules)
            : null;
    final UserEmailProjector projector =
        UserEmailProjector.builder()
            .host(getUsersFilteredQuery.getHost())
            .blockEmail(blockEmail)
            .blockedRules(blockedRules)
            .build();

    return transactionReadBoundary.execute(
        () ->
            Objects.isNull(paginationVo)
                ? userService.getAllUsers(projector)
                : userService.getAllUsers(projector, paginationVo));
  }
}
