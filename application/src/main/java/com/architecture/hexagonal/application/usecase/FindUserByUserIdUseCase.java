package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByUserIdUseCase implements FindUserByUserIdUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public User execute(FindUserByUserIdQuery findUserByUserIdQuery) {
    return userRepositoryReadPort.findUserById(findUserByUserIdQuery.getUserId());
  }
}
