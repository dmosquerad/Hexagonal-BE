package com.architecture.hexagonal.application.business.user.findbyid.usecase.impl;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindUserByUserIdUseCaseImpl implements FindUserByUserIdUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public User execute(final @NonNull FindUserByUserIdInput findUserByUserIdInput) {
    final UUID uuid = findUserByUserIdInput.userId();

    return userRepositoryReadPort
        .findUserById(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));
  }
}
