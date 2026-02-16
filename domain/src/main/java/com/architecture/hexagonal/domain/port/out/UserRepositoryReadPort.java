package com.architecture.hexagonal.domain.port.out;

import com.architecture.hexagonal.domain.data.UserDo;
import java.util.Set;
import java.util.UUID;

public interface UserRepositoryReadPort {
  Set<UserDo> getAllUsers();
  
  UserDo findUserById(UUID uuid);
}
