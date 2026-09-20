package com.architecture.hexagonal.application.usecase.business.user.patch.usecase;

import com.architecture.hexagonal.application.usecase.business.user.patch.input.PatchUserInput;
import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.NonNull;

public interface PatchUserUseCase {
  User execute(@NonNull PatchUserInput patchUserInput);
}
