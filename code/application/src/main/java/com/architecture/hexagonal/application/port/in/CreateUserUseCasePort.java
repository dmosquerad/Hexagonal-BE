package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.data.entity.User;

public interface CreateUserUseCasePort {
  User execute(CreateUserCommand createUserCommand);
}
