package com.architecture.hexagonal.application.feature.user.getall.usecase;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.application.common.pagination.PaginationResult;
import com.architecture.hexagonal.application.feature.user.getall.port.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQuery;
import com.architecture.hexagonal.application.feature.user.getall.query.GetAllUserQueryFiltered;
import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.Objects;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> execute(final GetAllUserQuery query) {
    final String host = query.getHost();
    if (Objects.isNull(query.getPagination())) {
      return userRepositoryReadPort.getAllUsers(host);
    }
    return userRepositoryReadPort.getAllUsers(host, query.getPagination());
  }

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> execute(final GetAllUserQueryFiltered getAllUserQueryFiltered) {
    final String host = getAllUserQueryFiltered.getHost();
    final Boolean blockEmail = getAllUserQueryFiltered.getBlockEmail();
    final Pagination pagination = getAllUserQueryFiltered.getPagination();
    final EmailBlockRulesVo blockedRules = getAllUserQueryFiltered.getBlockedRules();
    if (Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(host, blockEmail, blockedRules);
    }
    return userRepositoryReadPort.getAllUsers(host, blockEmail, blockedRules, pagination);
  }
}
