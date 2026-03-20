package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.port.in.UpdateUserUseCasePort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase implements UpdateUserUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  private final UserRepositoryWritePort userRepositoryWritePort;

  @Override
  @Transactional
  public User execute(UpdateUserCommand updateUserCommand) throws ResourceNotFoundException {
    final UUID uuid = updateUserCommand.getUserId();

    userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    return userRepositoryWritePort.saveUser(
        User.builder()
          .userId(uuid)
          .name(updateUserCommand.getName())
          .email(updateUserCommand.getEmail())
          .build());
  }
}