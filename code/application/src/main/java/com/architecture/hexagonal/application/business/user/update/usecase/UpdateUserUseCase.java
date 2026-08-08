package com.architecture.hexagonal.application.business.user.update.usecase;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserInput;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import lombok.NonNull;

public interface UpdateUserUseCase {
  User execute(@NonNull UpdateUserInput updateUserInput);
}
