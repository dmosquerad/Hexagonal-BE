package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.data.UserDo;

public interface FindUserByUserIdUseCasePort {
  UserDo execute(FindUserByUserIdQuery findUserByUserIdQuery);
}
