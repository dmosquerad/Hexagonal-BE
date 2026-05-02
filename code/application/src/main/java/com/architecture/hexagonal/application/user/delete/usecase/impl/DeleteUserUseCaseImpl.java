package com.architecture.hexagonal.application.user.delete.usecase.impl;

import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.application.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.application.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

  private final UserRepositoryWritePort userRepositoryWritePort;
  private final UserSenderPort userSenderPort;

  @Override
  public User execute(final DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException {
    final UUID uuid = deleteUserCommand.getUserId();

    User deletedUser =
        userRepositoryWritePort
            .deleteUser(uuid)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));

    userSenderPort.userSenderDeleted(deletedUser);
    return deletedUser;
  }
}
