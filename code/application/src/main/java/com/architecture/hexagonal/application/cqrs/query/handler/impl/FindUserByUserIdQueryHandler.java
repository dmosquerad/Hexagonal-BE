package com.architecture.hexagonal.application.cqrs.query.handler.impl;

import com.architecture.hexagonal.application.cqrs.query.handler.QueryHandler;
import com.architecture.hexagonal.application.cqrs.query.request.FindUserByUserIdQuery;
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
  public User handle(final FindUserByUserIdQuery query) throws Exception {
    return findUserByUserIdUseCasePort.execute(query);
  }
}
