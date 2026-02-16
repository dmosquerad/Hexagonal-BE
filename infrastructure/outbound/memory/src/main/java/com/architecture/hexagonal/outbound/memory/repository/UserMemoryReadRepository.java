package com.architecture.hexagonal.outbound.memory.repository;

import com.architecture.hexagonal.outbound.memory.data.UserDao;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserMemoryReadRepository {
  Set<UserDao> getAllUsers();

  Optional<UserDao> findUserById(UUID uuid);
}
