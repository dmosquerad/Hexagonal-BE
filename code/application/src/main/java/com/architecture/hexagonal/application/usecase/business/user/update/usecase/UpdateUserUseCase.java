package com.architecture.hexagonal.application.usecase.business.user.update.usecase;

import com.architecture.hexagonal.application.usecase.business.user.update.input.UpdateUserInput;
import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.NonNull;

public interface UpdateUserUseCase {
  User execute(@NonNull UpdateUserInput updateUserInput);
}
