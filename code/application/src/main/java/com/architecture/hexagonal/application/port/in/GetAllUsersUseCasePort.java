package com.architecture.hexagonal.application.port.in;

import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GetAllUsersUseCasePort {
  PaginationResult<User> execute(@Valid GetAllUserQuery getAllUserQuery);
}
