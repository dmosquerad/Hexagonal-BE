package com.architecture.hexagonal.application.port.out;

import com.architecture.hexagonal.domain.model.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryWritePort {
  User saveUser(User user);

  Optional<User> deleteUser(UUID uuid);
}
