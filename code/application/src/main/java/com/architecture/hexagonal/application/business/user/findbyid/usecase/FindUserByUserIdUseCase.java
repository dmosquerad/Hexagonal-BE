package com.architecture.hexagonal.application.business.user.findbyid.usecase;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.domain.model.aggregate.User;

public interface FindUserByUserIdUseCase {
  User execute(FindUserByUserIdInput findUserByUserIdInput);
}
