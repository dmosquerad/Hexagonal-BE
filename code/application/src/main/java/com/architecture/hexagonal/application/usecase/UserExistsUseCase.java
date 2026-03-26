package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.input.query.UserExistsQuery;
import com.architecture.hexagonal.application.port.in.UserExistsUseCasePort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.exception.ExceptionMessage;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserExistsUseCase implements UserExistsUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  @Transactional(readOnly = true)
  public void execute(final UserExistsQuery userExistsQuery) throws ResourceNotFoundException {
    final UUID uuid = userExistsQuery.getUserId();

    userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionMessage.NOT_FOUND_DATA_MESSAGE + uuid));
  }
}
