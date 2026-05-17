package com.architecture.hexagonal.application.feature.user.getfiltered.usecase;

import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.email.getblockedrules.port.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.application.feature.user.getall.port.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQuery;
import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQueryFiltered;
import com.architecture.hexagonal.application.feature.user.getfiltered.port.GetFilteredUsersUseCasePort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetFilteredUsersUseCase implements GetFilteredUsersUseCasePort {

  private final GetAllUsersUseCasePort getAllUsersUseCasePort;
  private final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> execute(final GetAllUserQuery query) {
    final GetAllUserQueryFiltered filteredQuery = GetAllUserQueryFiltered.builder()
        .host(query.getHost())
        .blockEmail(query.getBlockEmail())
        .pagination(query.getPagination())
        .blockedRules(getBlockedRulesUseCasePort.execute())
        .build();
    return getAllUsersUseCasePort.execute(filteredQuery);
  }
}
