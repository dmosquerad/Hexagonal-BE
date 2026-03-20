package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.input.command.PatchUserCommand;

public interface PatchUserUseCasePort {
  User execute(PatchUserCommand patchUserCommand) throws ResourceNotFoundException;
}