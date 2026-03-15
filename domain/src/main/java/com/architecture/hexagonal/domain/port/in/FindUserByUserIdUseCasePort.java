package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;

public interface FindUserByUserIdUseCasePort {
  User execute(FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException;
}
