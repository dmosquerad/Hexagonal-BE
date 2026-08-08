package com.architecture.hexagonal.application.usecase.business.user.create.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.usecase.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.application.usecase.business.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.model.vo.email.factory.EmailVoFactory;
import com.architecture.hexagonal.domain.service.EmailBlockPolicy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

  private final UserRepositoryWritePort userRepositoryWritePort;
  private final UserSenderPort userSenderPort;
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  public User execute(final @NonNull CreateUserInput createUserInput) {
    final EmailVo email = EmailVoFactory.from(createUserInput.email());

    if (EmailBlockPolicy.isBlocked(email, emailConfigurationPort.getBlockedRules())) {
      throw new InvalidValueException(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email.getEmail());
    }

    User createdUser =
        userRepositoryWritePort.saveUser(
            User.builder().name(createUserInput.name()).email(email).build());

    userSenderPort.userSenderCreated(createdUser);
    return createdUser;
  }
}
