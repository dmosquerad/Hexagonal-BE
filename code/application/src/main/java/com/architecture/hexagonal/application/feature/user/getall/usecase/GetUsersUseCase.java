package com.architecture.hexagonal.application.feature.user.getall.usecase;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.user.getall.port.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.feature.user.getall.query.GetUsersQuery;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.Objects;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> execute(final GetUsersQuery getUsersQuery) {
    final String host = getUsersQuery.getHost();
    final Boolean blockEmail = getUsersQuery.getBlockEmail();
    final Pagination pagination = getUsersQuery.getPagination();
    final EmailBlockRulesVo blockedRules = getUsersQuery.getBlockedRules();

    if (Objects.isNull(pagination) && Objects.isNull(blockedRules)) {
      return userRepositoryReadPort.getAllUsers(host);
    }
    if (Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(host, blockEmail, blockedRules);
    }

    if (Objects.isNull(blockEmail)) {
        return userRepositoryReadPort.getAllUsers(host, pagination);
    }

    return userRepositoryReadPort.getAllUsers(host, blockEmail, blockedRules, pagination);
  }
}
