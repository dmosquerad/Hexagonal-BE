package com.architecture.hexagonal.domain.port.out;

import com.architecture.hexagonal.domain.data.User;

public interface UserRepositoryWritePort {
  User saveUser(User user);
}
