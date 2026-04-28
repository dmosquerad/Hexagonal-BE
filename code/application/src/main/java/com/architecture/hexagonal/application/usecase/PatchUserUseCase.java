package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.cqrs.command.request.PatchUserCommand;
import com.architecture.hexagonal.application.port.in.PatchUserUseCasePort;
import com.architecture.hexagonal.application.port.out.UserSenderPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.vo.factory.EmailVoFactory;
import com.architecture.hexagonal.application.port.out.UserRepositoryWritePort;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatchUserUseCase implements PatchUserUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  private final UserRepositoryWritePort userRepositoryWritePort;

  private final UserSenderPort userSenderPort;

  @Override
  @Transactional
  public User execute(final PatchUserCommand patchUserCommand) throws ResourceNotFoundException {
    final UUID uuid = patchUserCommand.getUserId();

    final User currentUser = userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(() ->
            new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    final String name = StringUtils.isBlank(patchUserCommand.getName()) ?
            currentUser.getName() : patchUserCommand.getName();
    final EmailVo email = StringUtils.isBlank(patchUserCommand.getEmail()) ?
            currentUser.getEmail() : EmailVoFactory.from(patchUserCommand.getEmail());

    User updatedUser = userRepositoryWritePort.saveUser(
        User.builder()
            .userId(uuid)
            .name(name)
            .email(email)
            .build());

    userSenderPort.userSenderUpdated(updatedUser);
    return updatedUser;
  }
}