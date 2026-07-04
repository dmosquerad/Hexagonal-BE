package com.architecture.hexagonal.application.port.database;

import com.architecture.hexagonal.application.port.database.query.UserQuery;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryReadPort {

  PaginationResult<User> getAllUsers(@NonNull UserQuery userQuery, @NonNull Pagination pagination);

  PaginationResult<User> getAllUsers(@NonNull UserQuery userQuery);

  Optional<User> findUserById(@NonNull UUID uuid);
}
