package com.architecture.hexagonal.application.feature.user.getall.query.handler;

import com.architecture.hexagonal.application.common.cqrs.query.QueryHandler;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.user.getall.port.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQuery;
import com.architecture.hexagonal.application.feature.user.getfiltered.port.GetFilteredUsersUseCasePort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUserQuery, PaginationResult<User>> {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;
  private final GetFilteredUsersUseCasePort getFilteredUsersUseCasePort;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> handle(final GetAllUserQuery getAllUserQuery) {
    if (Objects.isNull(getAllUserQuery.getBlockEmail())) {
      return getAllUsersUseCasePort.execute(getAllUserQuery);
    }
    return getFilteredUsersUseCasePort.execute(getAllUserQuery);
  }
}
