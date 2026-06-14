package com.architecture.hexagonal.application.user.exists.usecase;

import com.architecture.hexagonal.application.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;

public interface UserExistsUseCase {
  void execute(UserExistsQuery userExistsQuery) throws ResourceNotFoundException;
}
