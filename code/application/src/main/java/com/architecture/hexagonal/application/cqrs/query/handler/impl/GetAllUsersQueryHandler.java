package com.architecture.hexagonal.application.cqrs.query.handler.impl;

import com.architecture.hexagonal.application.cqrs.query.handler.QueryHandler;
import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUserQuery, PaginationResult<User>> {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;

  @Override
  public PaginationResult<User> handle(final GetAllUserQuery query) {
    return getAllUsersUseCasePort.execute(query);
  }
}
