package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
import com.architecture.hexagonal.domain.model.aggregate.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.domain.model.projector.user.UserEmailProjector;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryReadPort {

  PaginationResult<User> getAllUsers(
      @NonNull UserEmailProjector userEmailProjector, @NonNull Pagination pagination);

  PaginationResult<User> getAllUsers(@NonNull UserEmailProjector userEmailProjector);

  Optional<User> findUserById(@NonNull UUID uuid);
}
