package com.architecture.hexagonal.application.cqrs.command.handler.impl;

import com.architecture.hexagonal.application.cqrs.command.handler.CommandHandler;
import com.architecture.hexagonal.application.cqrs.command.request.CreateUserCommand;
import com.architecture.hexagonal.application.port.in.CreateUserUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, User> {

  private final CreateUserUseCasePort createUserUseCasePort;

  @Override
  public Class<CreateUserCommand> getCommandType() {
    return CreateUserCommand.class;
  }

  @Override
  public User handle(final CreateUserCommand command) {
    return createUserUseCasePort.execute(command);
  }
}
