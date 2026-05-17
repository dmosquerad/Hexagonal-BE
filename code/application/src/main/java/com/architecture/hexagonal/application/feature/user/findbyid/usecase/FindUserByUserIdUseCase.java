package com.architecture.hexagonal.application.feature.user.findbyid.usecase;

import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.feature.user.findbyid.port.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.application.feature.user.findbyid.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindUserByUserIdUseCase implements FindUserByUserIdUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  @Transactional(readOnly = true)
  public User execute(final FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException {
    final UUID uuid = findUserByUserIdQuery.getUserId();

    return userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));
  }
}
