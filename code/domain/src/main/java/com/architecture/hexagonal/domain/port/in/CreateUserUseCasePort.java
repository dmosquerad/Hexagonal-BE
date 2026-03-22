package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.input.command.CreateUserCommand;

public interface CreateUserUseCasePort {
  User execute(CreateUserCommand createUserCommand);
}
