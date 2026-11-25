package com.architecture.hexagonal.application.usecase.user.command.update.impl;

import com.architecture.hexagonal.application.mapper.user.UserCommandMapper;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.contract.bus.model.user.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.annotation.ApplicationUseCase;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionWriteBoundary;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class UpdateUserUseCaseImpl implements CommandHandler<UpdateUserCommand, User> {

  private final TransactionWriteBoundary transactionWriteBoundary;
  private final EmailService emailService;
  private final UserService userService;

  @Override
  public User handle(final @NonNull UpdateUserCommand updateUserCommand) {
    final User user = UserCommandMapper.INSTANCE.toUser(updateUserCommand);
    return transactionWriteBoundary.execute(
        () -> {
          if (emailService.isEmailBlocked(user.email())) {
            throw new InvalidValueException(
                ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + user.email().getEmail(),
                FailureType.NON_RETRYABLE);
          }
          return userService.updateUser(user);
        });
  }
}
