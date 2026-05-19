package com.architecture.hexagonal.application.feature.user.getall.port;

import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.user.getall.query.GetUsersQuery;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GetAllUsersUseCasePort {

  PaginationResult<User> execute(@Valid GetUsersQuery getUsersQuery);
}
