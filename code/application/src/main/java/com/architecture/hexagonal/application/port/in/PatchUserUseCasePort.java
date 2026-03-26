package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.command.PatchUserCommand;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;

public interface PatchUserUseCasePort {
  User execute(PatchUserCommand patchUserCommand) throws ResourceNotFoundException;
}