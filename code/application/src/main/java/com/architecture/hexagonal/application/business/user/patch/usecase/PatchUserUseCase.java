package com.architecture.hexagonal.application.business.user.patch.usecase;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface PatchUserUseCase {
  User execute(PatchUserCommand patchUserCommand)
      throws ResourceNotFoundException, InvalidValueException;
}
