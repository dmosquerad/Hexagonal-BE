package com.architecture.hexagonal.application.business.user.create.usecase;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface CreateUserUseCase {
  User execute(CreateUserInput createUserInput);
}
