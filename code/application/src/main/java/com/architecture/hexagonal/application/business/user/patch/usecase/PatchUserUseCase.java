package com.architecture.hexagonal.application.business.user.patch.usecase;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserInput;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface PatchUserUseCase {
  User execute(PatchUserInput patchUserInput);
}
