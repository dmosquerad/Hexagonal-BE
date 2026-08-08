package com.architecture.hexagonal.application.usecase.business.user.getall.usecase.impl;

import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.application.usecase.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.application.usecase.business.user.getall.projector.UserEmailProjector;
import com.architecture.hexagonal.application.usecase.business.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.pagination.Pagination;
import com.architecture.hexagonal.domain.model.vo.pagination.PaginationResult;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public PaginationResult<User> execute(final @NonNull GetUsersInput getUsersInput) {
    final Pagination pagination = getUsersInput.pagination();

    final UserEmailProjector userEmailProjector =
        UserEmailProjector.builder()
            .host(getUsersInput.host())
            .blockEmail(getUsersInput.blockEmail())
            .blockedRules(getUsersInput.blockedRules())
            .build();

    if (Objects.isNull(pagination)) {
      return userRepositoryReadPort.getAllUsers(userEmailProjector);
    }

    return userRepositoryReadPort.getAllUsers(userEmailProjector, pagination);
  }
}
