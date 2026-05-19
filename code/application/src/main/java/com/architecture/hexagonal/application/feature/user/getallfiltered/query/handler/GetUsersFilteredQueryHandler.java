package com.architecture.hexagonal.application.feature.user.getallfiltered.query.handler;

import com.architecture.hexagonal.application.common.cqrs.query.QueryHandler;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.email.getblockedrules.port.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.application.feature.user.getall.port.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.feature.user.getallfiltered.query.GetUsersFilteredQuery;
import com.architecture.hexagonal.application.feature.user.getall.query.GetUsersQuery;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetUsersFilteredQueryHandler implements QueryHandler<GetUsersFilteredQuery, PaginationResult<User>> {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;
  private final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> handle(final GetUsersFilteredQuery getUsersFilteredQuery) {
    final GetUsersQuery filteredQuery = GetUsersQuery.builder()
            .host(getUsersFilteredQuery.getHost())
            .blockEmail(getUsersFilteredQuery.getBlockEmail())
            .pagination(getUsersFilteredQuery.getPagination())
            .blockedRules(Objects.nonNull(getUsersFilteredQuery.getBlockEmail()) ?
                    getBlockedRulesUseCasePort.execute() : null)
            .build();
    return getAllUsersUseCasePort.execute(filteredQuery);
  }
}
