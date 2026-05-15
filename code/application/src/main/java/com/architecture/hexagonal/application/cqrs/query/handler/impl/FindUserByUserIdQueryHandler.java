package com.architecture.hexagonal.application.cqrs.query.handler.impl;

import com.architecture.hexagonal.application.cqrs.query.handler.QueryHandler;
import com.architecture.hexagonal.application.cqrs.query.request.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.port.in.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserByUserIdQueryHandler implements QueryHandler<FindUserByUserIdQuery, User> {

  private final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  @Override
  public User handle(final FindUserByUserIdQuery query) throws ResourceNotFoundException {
    return findUserByUserIdUseCasePort.execute(query);
  }
}
