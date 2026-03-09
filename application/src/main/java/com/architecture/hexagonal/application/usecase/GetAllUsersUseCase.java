package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public Set<User> execute() {
    return userRepositoryReadPort.getAllUsers();
  }
}
