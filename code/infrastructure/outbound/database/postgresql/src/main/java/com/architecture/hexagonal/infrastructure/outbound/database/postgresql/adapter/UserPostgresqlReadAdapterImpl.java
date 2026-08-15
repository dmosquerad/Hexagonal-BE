package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.adapter;

import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
import com.architecture.hexagonal.domain.model.aggregate.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.domain.model.projector.user.UserEmailProjector;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserFromPostgresqlMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.UserPostgresqlReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.specification.UserSpecifications;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPostgresqlReadAdapterImpl implements UserRepositoryReadPort {
  private final UserPostgresqlReadRepository userPostgresqlReadRepository;

  private final UserFromPostgresqlMapper userFromPostgresqlMapper;

  @Override
  public PaginationResult<User> getAllUsers(
      final @NonNull UserEmailProjector userEmailProjector, final @NonNull Pagination pagination) {

    final Specification<UserDao> specification =
        Specification.where(UserSpecifications.hostEquals(userEmailProjector.host()))
            .and(
                UserSpecifications.blockedEmail(
                    userEmailProjector.blockEmail(), userEmailProjector.blockedRules()));

    final Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
    final Page<UserDao> page = userPostgresqlReadRepository.findAll(specification, pageable);

    return PaginationResult.<User>builder()
        .data(page.stream().map(userFromPostgresqlMapper::toUser).toList())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .page(page.getNumber())
        .size(page.getSize())
        .build();
  }

  @Override
  public PaginationResult<User> getAllUsers(final @NonNull UserEmailProjector userEmailProjector) {

    final Specification<UserDao> specification =
        Specification.where(UserSpecifications.hostEquals(userEmailProjector.host()))
            .and(
                UserSpecifications.blockedEmail(
                    userEmailProjector.blockEmail(), userEmailProjector.blockedRules()));

    final List<UserDao> results = userPostgresqlReadRepository.findAll(specification);
    final List<User> users = results.stream().map(userFromPostgresqlMapper::toUser).toList();
    return PaginationResult.<User>builder()
        .data(users)
        .totalElements(users.size())
        .totalPages(1)
        .page(0)
        .size(users.size())
        .build();
  }

  @Override
  public Optional<User> findUserById(final @NonNull UUID uuid) {
    return userPostgresqlReadRepository.findByUserId(uuid).map(userFromPostgresqlMapper::toUser);
  }
}
