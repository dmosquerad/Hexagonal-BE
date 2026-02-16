package com.architecture.hexagonal.application.usercase;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
import com.architecture.hexagonal.domain.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUserCase implements CreateUserUseCasePort {

  public final UserRepositoryWritePort userRepositoryWritePort;

  @Override
  public UserDo execute(CreateUserCommand createUserCommand) {
    return userRepositoryWritePort.createUser(
        UserDo.builder()
            .name(createUserCommand.getName())
            .email(createUserCommand.getEmail())
            .build());
  }
}
