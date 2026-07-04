package com.architecture.hexagonal.application.business.user.getall.usecase;

import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;

public interface GetAllUsersUseCase {

  PaginationResult<User> execute(GetUsersInput getUsersInput);
}
