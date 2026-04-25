package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.cqrs.command.request.DeleteUserCommand;
import com.architecture.hexagonal.application.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.application.port.out.UserSenderPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserUseCasePort {

  private final UserRepositoryWritePort userRepositoryWritePort;

  private final UserSenderPort userSenderPort;

  @Override
  @Transactional
  public User execute(final DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException {
    final UUID uuid = deleteUserCommand.getUserId();

    User deletedUser = userRepositoryWritePort.deleteUser(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    userSenderPort.userSenderDeleted(deletedUser);
    return deletedUser;
  }
}
