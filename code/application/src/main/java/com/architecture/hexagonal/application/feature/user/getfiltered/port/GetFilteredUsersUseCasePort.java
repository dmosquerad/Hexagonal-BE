package com.architecture.hexagonal.application.feature.user.getfiltered.port;

import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQuery;
import com.architecture.hexagonal.domain.model.aggregate.User;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GetFilteredUsersUseCasePort {

  PaginationResult<User> execute(@Valid GetAllUserQuery getAllUserQuery);
}
