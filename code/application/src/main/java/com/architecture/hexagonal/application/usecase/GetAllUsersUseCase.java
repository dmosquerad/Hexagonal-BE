package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.data.entity.User;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  @Transactional(readOnly = true)
  public Set<User> execute() {
    return userRepositoryReadPort.getAllUsers();
  }
}
