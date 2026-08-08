package com.architecture.hexagonal.application.business.user.delete.usecase;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import lombok.NonNull;

public interface DeleteUserUseCase {
  User execute(@NonNull DeleteUserInput deleteUserInput);
}
