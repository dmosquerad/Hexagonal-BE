package com.architecture.hexagonal.application.feature.user.findbyid.query.handler;

import com.architecture.hexagonal.application.feature.user.findbyid.query.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.common.cqrs.query.QueryHandler;
import com.architecture.hexagonal.application.feature.user.findbyid.port.FindUserByUserIdUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FindUserByUserIdQueryHandler implements QueryHandler<FindUserByUserIdQuery, User> {

  private final FindUserByUserIdUseCasePort findUserByUserIdUseCasePort;

  @Override
  @Transactional(readOnly = true)
  public User handle(final FindUserByUserIdQuery query) throws ResourceNotFoundException {
    return findUserByUserIdUseCasePort.execute(query);
  }
}
