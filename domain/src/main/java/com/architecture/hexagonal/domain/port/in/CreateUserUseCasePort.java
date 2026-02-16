package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.data.UserDo;

public interface CreateUserUseCasePort {
  UserDo execute(CreateUserCommand createUserCommand);
}
