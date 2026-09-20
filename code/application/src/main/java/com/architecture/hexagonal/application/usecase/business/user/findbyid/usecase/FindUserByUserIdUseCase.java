package com.architecture.hexagonal.application.usecase.business.user.findbyid.usecase;

import com.architecture.hexagonal.application.usecase.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.domain.model.entity.user.User;
import lombok.NonNull;

public interface FindUserByUserIdUseCase {
  User execute(@NonNull FindUserByUserIdInput findUserByUserIdInput);
}
