package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseWriteRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryDatabaseWriteAdapter implements UserRepositoryWritePort {
  private final UserDatabaseWriteRepository userDatabaseWriteRepository;

  private final UserMapper userMapper;

  private final UserDaoMapper userDaoMapper;

  @Override
  public User saveUser(final User user) {
    return userMapper.toUser(userDatabaseWriteRepository.save(userDaoMapper.toUserDao(user)));
  }

  @Override
  public Optional<User> deleteUser(final UUID uuid) {
    return userDatabaseWriteRepository.deleteByUserId(uuid).map(userMapper::toUser);
  }
}
