package com.architecture.hexagonal.application.business.user.getall.usecase;

import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.domain.model.aggregate.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import lombok.NonNull;

public interface GetAllUsersUseCase {

  PaginationResult<User> execute(@NonNull GetUsersInput getUsersInput);
}
