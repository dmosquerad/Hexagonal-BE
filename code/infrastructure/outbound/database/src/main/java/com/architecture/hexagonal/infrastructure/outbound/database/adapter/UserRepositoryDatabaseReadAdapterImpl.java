package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.application.port.database.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.user.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.specification.UserSpecifications;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryDatabaseReadAdapterImpl implements UserRepositoryReadPort {
  private final UserDatabaseReadRepository userDatabaseReadRepository;

  private final UserMapper userMapper;

  @Override
  public PaginationResult<User> getAllUsers(
      final @NonNull String host,
      final @NonNull Boolean blockEmail,
      final @NonNull EmailBlockRulesVo blockedRules,
      final @NonNull Pagination pagination) {

    final Specification<UserDao> specification =
        Specification.where(UserSpecifications.hostEquals(host))
            .and(UserSpecifications.blockedEmail(blockEmail, blockedRules));

    final Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
    final Page<UserDao> page = userDatabaseReadRepository.findAll(specification, pageable);

    return PaginationResult.<User>builder()
        .data(page.stream().map(userMapper::toUser).toList())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .page(page.getNumber())
        .size(page.getSize())
        .build();
  }

  @Override
  public PaginationResult<User> getAllUsers(
      final @NonNull String host,
      final @NonNull Boolean blockEmail,
      final @NonNull EmailBlockRulesVo blockedRules) {

    final Specification<UserDao> specification =
        Specification.where(UserSpecifications.hostEquals(host))
            .and(UserSpecifications.blockedEmail(blockEmail, blockedRules));

    final List<UserDao> results = userDatabaseReadRepository.findAll(specification);
    final List<User> users = results.stream().map(userMapper::toUser).toList();
    return PaginationResult.<User>builder()
        .data(users)
        .totalElements(users.size())
        .totalPages(1)
        .page(0)
        .size(users.size())
        .build();
  }

  @Override
  public PaginationResult<User> getAllUsers(
      final @NonNull String host, final @NonNull Pagination pagination) {

    final Specification<UserDao> specification =
        Specification.where(UserSpecifications.hostEquals(host))
            .and(UserSpecifications.blockedEmail());

    final Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
    final Page<UserDao> page = userDatabaseReadRepository.findAll(specification, pageable);

    return PaginationResult.<User>builder()
        .data(page.stream().map(userMapper::toUser).toList())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .page(page.getNumber())
        .size(page.getSize())
        .build();
  }

  @Override
  public PaginationResult<User> getAllUsers(final @NonNull String host) {

    final Specification<UserDao> specification =
        Specification.where(UserSpecifications.hostEquals(host))
            .and(UserSpecifications.blockedEmail());

    final List<UserDao> results = userDatabaseReadRepository.findAll(specification);
    final List<User> users = results.stream().map(userMapper::toUser).toList();
    return PaginationResult.<User>builder()
        .data(users)
        .totalElements(users.size())
        .totalPages(1)
        .page(0)
        .size(users.size())
        .build();
  }

  public PaginationResult<User> getAllUsers() {
    final List<UserDao> results = userDatabaseReadRepository.findAll();
    final List<User> users = results.stream().map(userMapper::toUser).toList();

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
    return userDatabaseReadRepository.findByUserId(uuid).map(userMapper::toUser);
  }
}
