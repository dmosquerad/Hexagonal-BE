package com.architecture.hexagonal.application.port.out;

import com.architecture.hexagonal.domain.model.aggregate.User;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

public interface UserRepositoryWritePort {
  User saveUser(@NonNull User user);

  Optional<User> deleteUser(@NonNull UUID uuid);
}
