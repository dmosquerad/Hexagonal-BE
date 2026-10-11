package com.architecture.hexagonal.application.port.out.user;

import com.architecture.hexagonal.application.projector.business.user.UserEmailProjector;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryReadPort {

  PaginationResultVo<User> getAllUsers(
      @NonNull UserEmailProjector userEmailProjector, @NonNull PaginationVo paginationVo);

  PaginationResultVo<User> getAllUsers(@NonNull UserEmailProjector userEmailProjector);

  Optional<User> findUserById(@NonNull UUID uuid);
}
