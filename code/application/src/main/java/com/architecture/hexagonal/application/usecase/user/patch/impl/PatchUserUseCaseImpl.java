package com.architecture.hexagonal.application.usecase.user.patch.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionWriteBoundary;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.application.service.user.UserService;
import com.architecture.hexagonal.contract.bus.model.user.PatchUserCommand;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@ApplicationUseCase
public class PatchUserUseCaseImpl implements CommandHandler<PatchUserCommand, User> {

  private final TransactionWriteBoundary transactionWriteBoundary;
  private final EmailService emailService;
  private final UserService userService;

  @Override
  public User handle(final @NonNull PatchUserCommand patchUserCommand) {
    final String email = patchUserCommand.getEmail();
    final User patch =
        User.builder()
            .userId(patchUserCommand.getUserId())
            .name(patchUserCommand.getName())
            .email(StringUtils.isBlank(email) ? null : EmailVoFactory.from(email))
            .build();
    return transactionWriteBoundary.execute(
        () -> {
          if (Objects.nonNull(patch.email()) && emailService.isEmailBlocked(patch.email())) {
            throw new InvalidValueException(
                ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + patch.email().getEmail());
          }
          return userService.patchUser(patch);
        });
  }
}
