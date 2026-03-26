package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.command.UpdateUserCommand;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;

public interface UpdateUserUseCasePort {
  User execute(UpdateUserCommand updateUserCommand) throws ResourceNotFoundException;
}