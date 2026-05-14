package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.application.cqrs.query.request.pagination.Pagination;
import com.architecture.hexagonal.application.cqrs.query.request.pagination.PaginationResult;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.specification.UserSpecifications;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryDatabaseReadAdapter implements UserRepositoryReadPort {
  private final UserDatabaseReadRepository userDatabaseReadRepository;

  private final UserMapper userMapper;

  @Override
  public PaginationResult<User> getAllUsers(
      final @NonNull String host,
      final @NonNull Boolean blockEmail,
      final @NonNull EmailBlockRulesVo blockedRules,
      final @NonNull Pagination pagination) {

    final Specification<UserDao> specification = Specification.where(UserSpecifications.hostEquals(host))
        .and(UserSpecifications.blockedEmail(blockEmail, blockedRules));

    final Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
    final Page<UserDao> page = userDatabaseReadRepository.findAll(specification, pageable);

    return PaginationResult.<User>builder()
        .data(page.stream().map(userMapper::toUser).collect(Collectors.toUnmodifiableSet()))
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

    final Specification<UserDao> specification = Specification.where(UserSpecifications.hostEquals(host))
        .and(UserSpecifications.blockedEmail(blockEmail, blockedRules));

    final List<UserDao> results = userDatabaseReadRepository.findAll(specification);
    final Set<User> mapped = results.stream().map(userMapper::toUser).collect(Collectors.toUnmodifiableSet());
    return PaginationResult.<User>builder()
        .data(mapped)
        .totalElements(mapped.size())
        .totalPages(1)
        .page(0)
        .size(mapped.size())
        .build();
  }

  @Override
  public PaginationResult<User> getAllUsers(final @NonNull String host, final @NonNull Pagination pagination) {

    final Specification<UserDao> specification = Specification.where(UserSpecifications.hostEquals(host))
        .and(UserSpecifications.blockedEmail());

    final Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
    final Page<UserDao> page = userDatabaseReadRepository.findAll(specification, pageable);

    return PaginationResult.<User>builder()
        .data(page.stream().map(userMapper::toUser).collect(Collectors.toUnmodifiableSet()))
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .page(page.getNumber())
        .size(page.getSize())
        .build();
  }

  @Override
  public PaginationResult<User> getAllUsers(final @NonNull String host) {

    final Specification<UserDao> specification = Specification.where(UserSpecifications.hostEquals(host))
        .and(UserSpecifications.blockedEmail());

    final List<UserDao> results = userDatabaseReadRepository.findAll(specification);
    final Set<User> mapped = results.stream().map(userMapper::toUser).collect(Collectors.toUnmodifiableSet());
    return PaginationResult.<User>builder()
        .data(mapped)
        .totalElements(mapped.size())
        .totalPages(1)
        .page(0)
        .size(mapped.size())
        .build();
  }

  @Override
  public Optional<User> findUserById(final @NonNull UUID uuid) {
    return userDatabaseReadRepository.findByUserId(uuid).map(userMapper::toUser);
  }
}
