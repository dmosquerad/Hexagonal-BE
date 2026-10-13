package com.architecture.hexagonal.application.usecase.user.delete.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class DeleteUserUseCaseImpl implements CommandHandler<DeleteUserCommand, User> {

  private final TransactionWriteBoundary transactionWriteBoundary;
  private final UserService userService;

  @Override
  public User handle(final @NonNull DeleteUserCommand deleteUserCommand) {
    return transactionWriteBoundary.execute(
        () -> userService.deleteUser(deleteUserCommand.getUserId()));
  }
}
