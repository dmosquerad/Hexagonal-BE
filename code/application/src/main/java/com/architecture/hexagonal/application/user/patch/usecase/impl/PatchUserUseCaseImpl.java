package com.architecture.hexagonal.application.user.patch.usecase.impl;

import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.user.patch.input.PatchUserCommand;
import com.architecture.hexagonal.application.user.patch.usecase.PatchUserUseCase;
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
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class PatchUserUseCaseImpl implements PatchUserUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;
  private final UserRepositoryWritePort userRepositoryWritePort;
  private final UserSenderPort userSenderPort;
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  public User execute(final PatchUserCommand patchUserCommand)
      throws ResourceNotFoundException, InvalidValueException {
    final UUID uuid = patchUserCommand.getUserId();

    final User currentUser =
        userRepositoryReadPort
            .findUserById(uuid)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    final EmailVo email =
        StringUtils.isBlank(patchUserCommand.getEmail())
            ? currentUser.getEmail()
            : EmailVoFactory.from(patchUserCommand.getEmail());

    if (EmailBlockPolicy.isBlocked(email, emailConfigurationPort.getBlockedRules())) {
      throw new InvalidValueException(ExceptionMessage.EMAIL_NO_ALLOWED_MESSAGE + email.getEmail());
    }

    final String name =
        StringUtils.isBlank(patchUserCommand.getName())
            ? currentUser.getUser().getName()
            : patchUserCommand.getName();

    User updatedUser =
        userRepositoryWritePort.saveUser(
            User.builder()
                .user(UserDo.builder().userId(uuid).name(name).build())
                .email(email)
                .build());

    userSenderPort.userSenderUpdated(updatedUser);
    return updatedUser;
  }
}
