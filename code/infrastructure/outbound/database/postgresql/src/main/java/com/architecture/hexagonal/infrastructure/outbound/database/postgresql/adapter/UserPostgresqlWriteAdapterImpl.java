package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.adapter;

import com.architecture.hexagonal.application.port.database.UserRepositoryWritePort;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user.UserFromPostgresqlMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository.UserPostgresqlWriteRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPostgresqlWriteAdapterImpl implements UserRepositoryWritePort {
  private final UserPostgresqlWriteRepository userPostgresqlWriteRepository;

  private final UserFromPostgresqlMapper userFromPostgresqlMapper;

  private final UserDaoMapper userDaoMapper;

  @Override
  public User saveUser(final @NonNull User user) {
    return userFromPostgresqlMapper.toUser(
        userPostgresqlWriteRepository.save(userDaoMapper.toUserDao(user)));
  }

  @Override
  public Optional<User> deleteUser(final @NonNull UUID uuid) {
    return userPostgresqlWriteRepository.deleteByUserId(uuid).map(userFromPostgresqlMapper::toUser);
  }
}
