package com.architecture.hexagonal.application.handler.query.impl;

import com.architecture.hexagonal.application.handler.query.QueryHandler;
import com.architecture.hexagonal.application.input.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserByUserIdQueryHandler implements QueryHandler<FindUserByUserIdQuery, User> {

  private final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  @Override
  public Class<FindUserByUserIdQuery> getQueryType() {
    return FindUserByUserIdQuery.class;
  }

  @Override
  public User handle(final FindUserByUserIdQuery query) throws ResourceNotFoundException {
    return findUserByUserIdUseCasePort.execute(query);
  }
}
