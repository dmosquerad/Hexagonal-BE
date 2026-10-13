package com.architecture.hexagonal.application.usecase.user.findbyid.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.contract.application.usecase.generated.user.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.model.entity.user.User;
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
