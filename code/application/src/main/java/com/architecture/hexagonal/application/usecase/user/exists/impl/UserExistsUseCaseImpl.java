package com.architecture.hexagonal.application.usecase.user.exists.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.contract.application.usecase.generated.user.UserExistsQuery;
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
