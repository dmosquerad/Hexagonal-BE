package com.architecture.hexagonal.application.feature.user.update.usecase;

import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.feature.user.update.command.UpdateUserCommand;
import com.architecture.hexagonal.application.feature.user.update.port.UpdateUserUseCasePort;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.entity.UserDo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.factory.EmailVoFactory;
import com.architecture.hexagonal.domain.service.EmailBlockPolicy;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase implements UpdateUserUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;
  private final UserRepositoryWritePort userRepositoryWritePort;
  private final UserSenderPort userSenderPort;
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  @Transactional
  public User execute(final UpdateUserCommand updateUserCommand) throws ResourceNotFoundException, InvalidValueException {
    final UUID uuid = updateUserCommand.getUserId();

    userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(() ->
            new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    final EmailVo email = EmailVoFactory.from(updateUserCommand.getEmail());

    if (EmailBlockPolicy.isBlocked(email, emailConfigurationPort.getBlockedRules())) {
      throw new InvalidValueException(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email.getEmail());
    }

    User updatedUser = userRepositoryWritePort.saveUser(
        User.builder()
            .user(UserDo.builder()
                .userId(uuid)
                .name(updateUserCommand.getName())
                .build())
            .email(email)
            .build());

    userSenderPort.userSenderUpdated(updatedUser);
    return updatedUser;
  }
}
