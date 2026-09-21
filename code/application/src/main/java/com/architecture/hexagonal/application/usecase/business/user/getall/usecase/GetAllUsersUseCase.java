package com.architecture.hexagonal.application.usecase.business.user.getall.usecase;

import com.architecture.hexagonal.application.usecase.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResult;
import lombok.NonNull;

public interface GetAllUsersUseCase {

  PaginationResult<User> execute(@NonNull GetUsersInput getUsersInput);
}
