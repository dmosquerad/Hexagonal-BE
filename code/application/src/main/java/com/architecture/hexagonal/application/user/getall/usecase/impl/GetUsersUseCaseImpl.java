package com.architecture.hexagonal.application.user.getall.usecase.impl;

import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.user.getall.input.GetUsersQuery;
import com.architecture.hexagonal.application.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetUsersUseCaseImpl implements GetAllUsersUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public PaginationResult<User> execute(final GetUsersQuery getUsersQuery) {
    final String host = getUsersQuery.getHost();
    final Boolean blockEmail = getUsersQuery.getBlockEmail();
    final Pagination pagination = getUsersQuery.getPagination();
    final EmailBlockRulesVo blockedRules = getUsersQuery.getBlockedRules();

    if (Objects.isNull(pagination) && Objects.isNull(blockedRules) && Objects.isNull(host)) {
      return userRepositoryReadPort.getAllUsers();
    }

    if (Objects.isNull(pagination) && Objects.isNull(blockedRules)) {
      return userRepositoryReadPort.getAllUsers(host);
    }

    if (Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(host, blockEmail, blockedRules);
    }

    if (Objects.isNull(blockEmail)) {
      return userRepositoryReadPort.getAllUsers(host, pagination);
    }

    return userRepositoryReadPort.getAllUsers(host, blockEmail, blockedRules, pagination);
  }
}
