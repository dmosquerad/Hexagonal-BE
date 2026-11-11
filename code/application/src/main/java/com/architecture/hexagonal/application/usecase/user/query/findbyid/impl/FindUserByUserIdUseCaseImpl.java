package com.architecture.hexagonal.application.usecase.user.query.findbyid.impl;

import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.contract.bus.model.user.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.annotation.ApplicationUseCase;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionReadBoundary;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class FindUserByUserIdUseCaseImpl implements QueryHandler<FindUserByUserIdQuery, User> {

  private final TransactionReadBoundary transactionReadBoundary;
  private final UserService userService;

  @Override
  public User handle(final @NonNull FindUserByUserIdQuery findUserByUserIdQuery) {
    return transactionReadBoundary.execute(
        () -> userService.findUserById(findUserByUserIdQuery.getUserId()));
  }
}
