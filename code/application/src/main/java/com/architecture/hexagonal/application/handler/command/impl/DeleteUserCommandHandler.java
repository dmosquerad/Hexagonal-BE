package com.architecture.hexagonal.application.handler.command.impl;

import com.architecture.hexagonal.application.handler.command.CommandHandler;
import com.architecture.hexagonal.application.input.command.DeleteUserCommand;
import com.architecture.hexagonal.application.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand, User> {

  private final DeleteUserUseCasePort deleteUserUseCasePort;

  @Override
  public Class<DeleteUserCommand> getCommandType() {
    return DeleteUserCommand.class;
  }

  @Override
  public User handle(final DeleteUserCommand command) throws Exception {
    return deleteUserUseCasePort.execute(command);
  }
}
