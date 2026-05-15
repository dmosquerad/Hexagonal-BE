package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.Pagination;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> execute(final GetAllUserQuery getAllUserQuery) {
    final String host = getAllUserQuery.getHost();
    final Boolean blockEmail = getAllUserQuery.getBlockEmail();
    final Pagination pagination = getAllUserQuery.getPagination();

    if (Objects.isNull(blockEmail) && Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(host);
    }
    if (Objects.isNull(blockEmail)) {
      return userRepositoryReadPort.getAllUsers(host, pagination);
    }
    if (Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(host, blockEmail, emailConfigurationPort.getBlockedRules());
    }
    return userRepositoryReadPort.getAllUsers(host, blockEmail, emailConfigurationPort.getBlockedRules(), pagination);
  }
}
