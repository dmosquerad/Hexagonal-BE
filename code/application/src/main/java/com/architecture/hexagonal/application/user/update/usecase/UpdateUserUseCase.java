package com.architecture.hexagonal.application.user.update.usecase;

import com.architecture.hexagonal.application.user.update.input.UpdateUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface UpdateUserUseCase {
  User execute(UpdateUserCommand updateUserCommand)
      throws ResourceNotFoundException, InvalidValueException;
}
