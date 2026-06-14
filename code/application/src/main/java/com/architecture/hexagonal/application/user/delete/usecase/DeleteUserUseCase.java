package com.architecture.hexagonal.application.user.delete.usecase;

import com.architecture.hexagonal.application.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface DeleteUserUseCase {
  User execute(DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException;
}
