package com.architecture.hexagonal.application.business.user.exists.usecase;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;
import lombok.NonNull;

public interface UserExistsUseCase {
  void execute(@NonNull UserExistsInput userExistsInput);
}
