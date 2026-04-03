package com.architecture.hexagonal.application.cqrs.command.handler.impl;

import com.architecture.hexagonal.application.cqrs.command.handler.CommandHandler;
import com.architecture.hexagonal.application.cqrs.command.request.UpdateUserCommand;
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
