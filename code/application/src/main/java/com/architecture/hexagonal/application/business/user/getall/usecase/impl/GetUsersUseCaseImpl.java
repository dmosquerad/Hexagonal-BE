package com.architecture.hexagonal.application.business.user.getall.usecase.impl;

import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.application.business.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.database.query.UserQuery;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetUsersUseCaseImpl implements GetAllUsersUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public PaginationResult<User> execute(final GetUsersInput getUsersInput) {
    final Pagination pagination = getUsersInput.getPagination();

    final UserQuery userQuery =
        UserQuery.builder()
            .host(getUsersInput.getHost())
            .blockEmail(getUsersInput.getBlockEmail())
            .blockedRules(getUsersInput.getBlockedRules())
            .build();

    if (Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(userQuery);
    }

    return userRepositoryReadPort.getAllUsers(userQuery, pagination);
  }
}
