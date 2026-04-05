package com.architecture.hexagonal.application.cqrs.command.handler.impl;

import com.architecture.hexagonal.application.cqrs.command.handler.CommandHandler;
import com.architecture.hexagonal.application.cqrs.command.request.PatchUserCommand;
import com.architecture.hexagonal.application.port.in.PatchUserUseCasePort;
import com.architecture.hexagonal.domain.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatchUserCommandHandler implements CommandHandler<PatchUserCommand, User> {

  private final PatchUserUseCasePort patchUserUseCasePort;

  @Override
  public Class<PatchUserCommand> getCommandType() {
    return PatchUserCommand.class;
  }

  @Override
  public User handle(final PatchUserCommand command) throws Exception {
    return patchUserUseCasePort.execute(command);
  }
}
