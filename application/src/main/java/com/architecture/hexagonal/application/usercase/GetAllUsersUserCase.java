package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.domain.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllUsersUserCase implements GetAllUsersUseCasePort {

  public final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  public Set<UserDo> execute() {
    return userRepositoryReadPort.getAllUsers();
  }
}
