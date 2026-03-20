package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.query.UserExistsQuery;

public interface UserExistsUseCasePort {
  void execute(UserExistsQuery userExistsQuery) throws ResourceNotFoundException;
}
