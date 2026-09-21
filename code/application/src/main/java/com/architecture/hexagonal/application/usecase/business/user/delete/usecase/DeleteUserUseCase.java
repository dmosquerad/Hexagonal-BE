package com.architecture.hexagonal.application.usecase.business.user.delete.usecase;

import com.architecture.hexagonal.application.usecase.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.NonNull;

public interface DeleteUserUseCase {
  User execute(@NonNull DeleteUserInput deleteUserInput);
}
