package com.architecture.hexagonal.application.usecase.business.user.create.usecase;

import com.architecture.hexagonal.application.usecase.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.NonNull;

public interface CreateUserUseCase {
  User execute(@NonNull CreateUserInput createUserInput);
}
