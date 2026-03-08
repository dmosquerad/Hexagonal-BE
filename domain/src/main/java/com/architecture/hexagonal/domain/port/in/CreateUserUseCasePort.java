package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.data.User;

public interface CreateUserUseCasePort {
  User execute(CreateUserCommand createUserCommand);
}
