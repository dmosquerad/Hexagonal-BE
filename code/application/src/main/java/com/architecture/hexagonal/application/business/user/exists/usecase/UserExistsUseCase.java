package com.architecture.hexagonal.application.business.user.exists.usecase;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;

public interface UserExistsUseCase {
  void execute(UserExistsInput userExistsInput);
}
