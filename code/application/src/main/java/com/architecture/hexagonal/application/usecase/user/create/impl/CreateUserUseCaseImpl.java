package com.architecture.hexagonal.application.usecase.user.create.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.contract.application.usecase.generated.user.CreateUserCommand;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class CreateUserUseCaseImpl implements CommandHandler<CreateUserCommand, User> {

  private final TransactionWriteBoundary transactionWriteBoundary;
  private final EmailService emailService;
  private final UserService userService;

  @Override
  public User handle(final @NonNull CreateUserCommand createUserCommand) {
    final User user =
        User.builder()
            .email(EmailVoFactory.from(createUserCommand.getEmail()))
            .name(createUserCommand.getName())
            .build();
    return transactionWriteBoundary.execute(
        () -> {
          if (emailService.isEmailBlocked(user.email())) {
            throw new InvalidValueException(
                ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + user.email().getEmail());
          }
          return userService.createUser(user);
        });
  }
}
