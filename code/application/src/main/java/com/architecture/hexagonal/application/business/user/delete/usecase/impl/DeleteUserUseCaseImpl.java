package com.architecture.hexagonal.application.business.user.delete.usecase.impl;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.application.business.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

  private final UserRepositoryWritePort userRepositoryWritePort;
  private final UserSenderPort userSenderPort;

  @Override
  public User execute(final @NonNull DeleteUserInput deleteUserInput) {
    final UUID uuid = deleteUserInput.userId();

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
