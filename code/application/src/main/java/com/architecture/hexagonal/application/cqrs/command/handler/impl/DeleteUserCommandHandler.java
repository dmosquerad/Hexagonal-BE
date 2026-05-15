package com.architecture.hexagonal.application.cqrs.command.handler.impl;

import com.architecture.hexagonal.application.cqrs.command.handler.CommandHandler;
import com.architecture.hexagonal.application.cqrs.command.request.DeleteUserCommand;
import com.architecture.hexagonal.application.port.in.DeleteUserUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand, User> {

  private final DeleteUserUseCasePort deleteUserUseCasePort;

  @Override
  public User handle(final DeleteUserCommand command) throws ResourceNotFoundException {
    return deleteUserUseCasePort.execute(command);
  }
}
