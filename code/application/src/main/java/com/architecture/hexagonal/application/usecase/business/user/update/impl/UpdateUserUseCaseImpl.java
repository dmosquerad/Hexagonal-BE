package com.architecture.hexagonal.application.usecase.business.user.update.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.application.service.business.user.UserService;
import com.architecture.hexagonal.contract.application.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
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
    final User user =
        User.builder()
            .userId(updateUserCommand.getUserId())
            .name(updateUserCommand.getName())
            .email(EmailVoFactory.from(updateUserCommand.getEmail()))
            .build();
    return transactionWriteBoundary.execute(
        () -> {
          if (emailService.isEmailBlocked(user.email())) {
            throw new InvalidValueException(
                ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + user.email().getEmail());
          }
          return userService.updateUser(user);
        });
  }
}
