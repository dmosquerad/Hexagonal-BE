package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.query.UserExistsQuery;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;

public interface UserExistsUseCasePort {
  void execute(UserExistsQuery userExistsQuery) throws ResourceNotFoundException;
}
