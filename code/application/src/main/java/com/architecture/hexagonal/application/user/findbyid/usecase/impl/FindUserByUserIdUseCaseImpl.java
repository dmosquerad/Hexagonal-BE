package com.architecture.hexagonal.application.user.findbyid.usecase.impl;

import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindUserByUserIdUseCaseImpl implements FindUserByUserIdUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public User execute(final FindUserByUserIdQuery findUserByUserIdQuery)
      throws ResourceNotFoundException {
    final UUID uuid = findUserByUserIdQuery.getUserId();

    return userRepositoryReadPort
        .findUserById(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));
  }
}
