package com.architecture.hexagonal.application.handler.query.impl;

import com.architecture.hexagonal.application.handler.query.QueryHandler;
import com.architecture.hexagonal.application.input.query.UserExistsQuery;
import com.architecture.hexagonal.application.port.in.UserExistsUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistsQueryHandler implements QueryHandler<UserExistsQuery, Void> {

  private final UserExistsUseCasePort userExistsUseCasePort;

  @Override
  public Class<UserExistsQuery> getQueryType() {
    return UserExistsQuery.class;
  }

  @Override
  public Void handle(final UserExistsQuery query) throws ResourceNotFoundException {
    userExistsUseCasePort.execute(query);
    return null;
  }
}
