package com.architecture.hexagonal.domain.port.out;

import com.architecture.hexagonal.domain.data.User;
import java.util.Set;
import java.util.UUID;

public interface UserRepositoryReadPort {
  Set<User> getAllUsers();
  
  User findUserById(UUID uuid);
}
