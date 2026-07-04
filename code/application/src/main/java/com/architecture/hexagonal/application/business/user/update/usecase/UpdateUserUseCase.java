package com.architecture.hexagonal.application.business.user.update.usecase;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserInput;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface UpdateUserUseCase {
  User execute(UpdateUserInput updateUserInput);
}
