package com.architecture.hexagonal.application.business.user.create.usecase;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface CreateUserUseCase {
  User execute(CreateUserCommand createUserCommand) throws InvalidValueException;
}
