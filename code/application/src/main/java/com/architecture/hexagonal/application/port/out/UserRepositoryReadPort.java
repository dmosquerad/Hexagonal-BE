package com.architecture.hexagonal.application.port.out;

import com.architecture.hexagonal.application.cqrs.query.request.pagination.Pagination;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryReadPort {
  PaginationResult<User> getAllUsers(
      @NonNull String host,
      @NonNull Boolean blockEmail,
      @NonNull EmailBlockRulesVo blockedRules,
      @NonNull Pagination pagination);

  PaginationResult<User> getAllUsers(
      @NonNull String host,
      @NonNull Boolean blockEmail,
      @NonNull EmailBlockRulesVo blockedRules);

  PaginationResult<User> getAllUsers(@NonNull String host, @NonNull Pagination pagination);

  PaginationResult<User> getAllUsers(@NonNull String host);

  Optional<User> findUserById(@NonNull UUID uuid);
}
