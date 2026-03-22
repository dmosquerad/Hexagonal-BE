package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryDatabaseReadAdapter implements UserRepositoryReadPort {
  private final UserDatabaseReadRepository userDatabaseReadRepository;

  private final UserMapper userMapper;

  @Override
  public Set<User> getAllUsers() {
    return userDatabaseReadRepository.findAll().stream()
        .map(userMapper::toUser)
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Optional<User> findUserById(final UUID uuid) {
    return userDatabaseReadRepository.findByUserId(uuid).map(userMapper::toUser);
  }
}
