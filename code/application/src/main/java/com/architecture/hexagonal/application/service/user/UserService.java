package com.architecture.hexagonal.application.service.user;

import com.architecture.hexagonal.application.projector.user.UserEmailProjector;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResultVo;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationVo;
import java.util.UUID;
import lombok.NonNull;

public interface UserService {
  User createUser(@NonNull User user);

  User deleteUser(@NonNull UUID userUuid);

  void existsUser(@NonNull UUID userUuid);

  User findUserById(@NonNull UUID userUuid);

  PaginationResultVo<User> getAllUsers(@NonNull UserEmailProjector userEmailProjector);

  PaginationResultVo<User> getAllUsers(
      @NonNull UserEmailProjector userEmailProjector, @NonNull PaginationVo paginationVo);

  User patchUser(@NonNull User user);

  User updateUser(@NonNull User user);
}
