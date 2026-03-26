package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;

public interface FindUserByUserIdUseCasePort {
  User execute(FindUserByUserIdQuery findUserByUserIdQuery) throws ResourceNotFoundException;
}
