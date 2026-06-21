package com.architecture.hexagonal.application.business.user.exists.usecase.impl;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.application.business.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserExistsUseCaseImpl implements UserExistsUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public void execute(final UserExistsQuery userExistsQuery) throws ResourceNotFoundException {
    final UUID uuid = userExistsQuery.getUserId();

    userRepositoryReadPort
        .findUserById(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));
  }
}
