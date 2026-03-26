package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.input.command.CreateUserCommand;
import com.architecture.hexagonal.application.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.application.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.domain.service.factory.vo.EmailVoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserUseCasePort {

  private final UserRepositoryWritePort userRepositoryWritePort;

  @Override
  @Transactional
  public User execute(final CreateUserCommand createUserCommand) {
    return userRepositoryWritePort.saveUser(
        User.builder()
            .name(createUserCommand.getName())
            .email(EmailVoFactory.from(createUserCommand.getEmail()))
            .build());
  }
}
