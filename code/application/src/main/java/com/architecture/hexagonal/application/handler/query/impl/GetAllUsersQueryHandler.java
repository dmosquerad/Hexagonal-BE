package com.architecture.hexagonal.application.handler.query.impl;

import com.architecture.hexagonal.application.handler.query.QueryHandler;
import com.architecture.hexagonal.application.input.query.GetAllUserQuery;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUserQuery, Set<User>> {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;

  @Override
  public Class<GetAllUserQuery> getQueryType() {
    return GetAllUserQuery.class;
  }

  @Override
  public Set<User> handle(final GetAllUserQuery query) {
    return getAllUsersUseCasePort.execute(query);
  }
}
