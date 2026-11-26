package com.architecture.hexagonal.application.usecase.user.command.patch.impl;

import com.architecture.hexagonal.application.mapper.user.UserMapper;
import com.architecture.hexagonal.application.model.command.user.PatchUserCommand;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.domain.annotation.ApplicationUseCase;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import com.architecture.hexagonal.domain.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.domain.port.in.transaction.TransactionWriteBoundary;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class PatchUserUseCaseImpl implements CommandHandler<PatchUserCommand, User> {

  private final TransactionWriteBoundary transactionWriteBoundary;
  private final EmailService emailService;
  private final UserService userService;

  @Override
  public User handle(final @NonNull PatchUserCommand patchUserCommand) {
    final User patch = UserMapper.INSTANCE.toUser(patchUserCommand);
    return transactionWriteBoundary.execute(
        () -> {
          if (Objects.nonNull(patch.email()) && emailService.isEmailBlocked(patch.email())) {
            throw new InvalidValueException(
                ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + patch.email().getEmail(),
                FailureType.NON_RETRYABLE);
          }
          return userService.patchUser(patch);
        });
  }
}
