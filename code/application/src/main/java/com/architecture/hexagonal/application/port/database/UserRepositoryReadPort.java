package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.application.usecase.business.user.getall.projector.UserEmailProjector;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.Pagination;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResult;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryReadPort {

  PaginationResult<User> getAllUsers(
      @NonNull UserEmailProjector userEmailProjector, @NonNull Pagination pagination);

  PaginationResult<User> getAllUsers(@NonNull UserEmailProjector userEmailProjector);

  Optional<User> findUserById(@NonNull UUID uuid);
}
