package com.architecture.hexagonal.application.handler.command.impl;

import com.architecture.hexagonal.application.handler.command.CommandHandler;
import com.architecture.hexagonal.application.input.command.UpdateUserCommand;
import com.architecture.hexagonal.application.port.in.UpdateUserUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, User> {

  private final UpdateUserUseCasePort updateUserUseCasePort;

  @Override
  public Class<UpdateUserCommand> getCommandType() {
    return UpdateUserCommand.class;
  }

  @Override
  public User handle(final UpdateUserCommand command) throws Exception {
    return updateUserUseCasePort.execute(command);
  }
}
