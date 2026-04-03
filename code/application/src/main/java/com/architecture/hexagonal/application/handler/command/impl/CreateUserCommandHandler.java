package com.architecture.hexagonal.application.handler.command.impl;

import com.architecture.hexagonal.application.handler.command.CommandHandler;
import com.architecture.hexagonal.application.input.command.CreateUserCommand;
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
  public User handle(final CreateUserCommand command) throws Exception {
    return createUserUseCasePort.execute(command);
  }
}
