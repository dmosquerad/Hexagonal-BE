package com.architecture.hexagonal.application.user.getall.usecase;

import com.architecture.hexagonal.application.user.getall.input.GetUsersQuery;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;

public interface GetAllUsersUseCase {

  PaginationResult<User> execute(GetUsersQuery getUsersQuery);
}
