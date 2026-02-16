package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByUserIdUserCase implements FindUserByUserIdUseCasePort {

  public final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public UserDo execute(FindUserByUserIdQuery findUserByUserIdQuery) {
    return userRepositoryReadPort.findUserById(findUserByUserIdQuery.getUserId());
  }
}
