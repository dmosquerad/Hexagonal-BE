package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.command.DeleteUserCommand;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;

public interface DeleteUserUseCasePort {
  User execute(DeleteUserCommand deleteUserCommand) throws ResourceNotFoundException;
}
