package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserUseCasePort {

  private final UserRepositoryWritePort userRepositoryWritePort;

  @Override
  @Transactional
  public User execute(DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException {
    final UUID uuid = deleteUserCommand.getUserId();

    return userRepositoryWritePort.deleteUser(uuid)
        .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));
  }
}
