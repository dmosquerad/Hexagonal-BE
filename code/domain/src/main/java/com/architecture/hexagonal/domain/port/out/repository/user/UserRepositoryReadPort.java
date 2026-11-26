package com.architecture.hexagonal.domain.port.out.repository.user;

import com.architecture.hexagonal.domain.model.criteria.user.UserEmailCriteria;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryReadPort {

  PaginationResultVo<User> getAllUsers(
      @NonNull UserEmailCriteria userEmailCriteria, @NonNull PaginationVo paginationVo);

  PaginationResultVo<User> getAllUsers(@NonNull UserEmailCriteria userEmailCriteria);

  Optional<User> findUserById(@NonNull UUID uuid);
}
