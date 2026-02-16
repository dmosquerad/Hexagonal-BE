package com.architecture.hexagonal.outbound.memory.repository.impl;

import com.architecture.hexagonal.outbound.memory.data.UserDao;
import com.architecture.hexagonal.outbound.memory.repository.UserMemoryReadRepository;
import com.architecture.hexagonal.outbound.memory.storage.UserMemoryStorage;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserMemoryReadRepositoryImpl implements UserMemoryReadRepository {

  private final UserMemoryStorage userMemoryStorage;

  @Override
  public Set<UserDao> getAllUsers() {
    return userMemoryStorage.getUsers();
  }

  @Override
  public Optional<UserDao> findUserById(UUID uuid) {
    return userMemoryStorage.getUsers().stream()
        .filter(userDao -> userDao.getUserId().equals(uuid))
        .findFirst();
  }
}
