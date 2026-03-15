package com.architecture.hexagonal.domain.port.out;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryWritePort {
  User saveUser(User user);

  Optional<User> deleteUser(UUID uuid);
}
