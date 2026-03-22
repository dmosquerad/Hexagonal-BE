package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.data.entity.User;
import java.util.Set;

public interface GetAllUsersUseCasePort {
  Set<User> execute();
}
