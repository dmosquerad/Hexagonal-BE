package com.architecture.hexagonal.application.feature.user.create.usecase;

import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.feature.user.create.command.CreateUserCommand;
import com.architecture.hexagonal.application.feature.user.create.port.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.entity.UserDo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.factory.EmailVoFactory;
import com.architecture.hexagonal.domain.service.EmailBlockPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserUseCasePort {

  private final UserRepositoryWritePort userRepositoryWritePort;
  private final UserSenderPort userSenderPort;
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  @Transactional
  public User execute(final CreateUserCommand createUserCommand) throws InvalidValueException {
    final EmailVo email = EmailVoFactory.from(createUserCommand.getEmail());

    if (EmailBlockPolicy.isBlocked(email, emailConfigurationPort.getBlockedRules())) {
      throw new InvalidValueException(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email.getEmail());
    }

    User createdUser = userRepositoryWritePort.saveUser(
        User.builder()
            .user(UserDo.builder()
                .name(createUserCommand.getName())
                .build())
            .email(email)
            .build());

    userSenderPort.userSenderCreated(createdUser);
    return createdUser;
  }
}
