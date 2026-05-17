package com.architecture.hexagonal.application.feature.user.create.command.handler;

import com.architecture.hexagonal.application.feature.user.create.command.CreateUserCommand;
import com.architecture.hexagonal.application.common.cqrs.command.CommandHandler;
import com.architecture.hexagonal.application.feature.user.create.port.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, User> {

  private final CreateUserUseCasePort createUserUseCasePort;

  @Override
  @Transactional
  public User handle(final CreateUserCommand command) throws InvalidValueException {
    return createUserUseCasePort.execute(command);
  }
}
