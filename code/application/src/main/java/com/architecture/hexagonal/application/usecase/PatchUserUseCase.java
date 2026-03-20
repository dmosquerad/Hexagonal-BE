package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.PatchUserCommand;
import com.architecture.hexagonal.domain.port.in.PatchUserUseCasePort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatchUserUseCase implements PatchUserUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  private final UserRepositoryWritePort userRepositoryWritePort;

  @Override
  @Transactional
  public User execute(final PatchUserCommand patchUserCommand) throws ResourceNotFoundException {
    final UUID uuid = patchUserCommand.getUserId();

    final User currentUser = userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(() ->
            new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    return userRepositoryWritePort.saveUser(
        User.builder()
            .userId(uuid)
            .name(Optional.ofNullable(patchUserCommand.getName()).orElse(currentUser.getName()))
            .email(Optional.ofNullable(patchUserCommand.getEmail()).orElse(currentUser.getEmail()))
            .build());
  }
}