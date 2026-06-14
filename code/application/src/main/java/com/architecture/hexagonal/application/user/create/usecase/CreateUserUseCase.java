package com.architecture.hexagonal.application.user.create.usecase;

import com.architecture.hexagonal.application.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface CreateUserUseCase {
  User execute(CreateUserCommand createUserCommand) throws InvalidValueException;
}
