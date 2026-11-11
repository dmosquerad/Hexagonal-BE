package com.architecture.hexagonal.application.usecase.user.query.exists.impl;

import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.contract.bus.model.user.query.UserExistsQuery;
import com.architecture.hexagonal.domain.annotation.ApplicationUseCase;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionReadBoundary;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class UserExistsUseCaseImpl implements QueryHandler<UserExistsQuery, Void> {

  private final TransactionReadBoundary transactionReadBoundary;
  private final UserService userService;

  @Override
  public Void handle(final @NonNull UserExistsQuery userExistsQuery) {
    return transactionReadBoundary.execute(
        () -> {
          userService.existsUser(userExistsQuery.getUserId());
          return null;
        });
  }
}
