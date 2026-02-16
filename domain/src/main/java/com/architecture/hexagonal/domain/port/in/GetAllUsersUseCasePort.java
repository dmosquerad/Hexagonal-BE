package com.architecture.hexagonal.domain.port.in;

import com.architecture.hexagonal.domain.data.UserDo;
import java.util.Set;

public interface GetAllUsersUseCasePort {
  Set<UserDo> execute();
}
