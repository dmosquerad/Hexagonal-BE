package com.architecture.hexagonal.application.feature.user.exists.query.handler;

import com.architecture.hexagonal.application.feature.user.exists.query.UserExistsQuery;
import com.architecture.hexagonal.application.common.cqrs.query.QueryHandler;
import com.architecture.hexagonal.application.feature.user.exists.port.UserExistsUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserExistsQueryHandler implements QueryHandler<UserExistsQuery, Void> {

  private final UserExistsUseCasePort userExistsUseCasePort;

  @Override
  @Transactional(readOnly = true)
  public Void handle(final UserExistsQuery query) throws ResourceNotFoundException {
    userExistsUseCasePort.execute(query);
    return null;
  }
}
