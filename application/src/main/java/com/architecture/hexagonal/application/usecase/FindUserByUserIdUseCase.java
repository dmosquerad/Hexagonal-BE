package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByUserIdUseCase implements FindUserByUserIdUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public User execute(FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException {
    final UUID uuid = findUserByUserIdQuery.getUserId();

    return userRepositoryReadPort.findUserById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("User not found by uuid: " + uuid));
  }
}
