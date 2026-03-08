package com.architecture.hexagonal.domain.port.out;

import com.architecture.hexagonal.domain.data.UserDo;

public interface UserRepositoryWritePort {
  UserDo saveUser(UserDo userDo);
}
