package com.architecture.hexagonal.application.feature.user.delete.command.handler;

import com.architecture.hexagonal.application.feature.user.delete.command.DeleteUserCommand;
import com.architecture.hexagonal.application.common.cqrs.command.CommandHandler;
import com.architecture.hexagonal.application.feature.user.delete.port.DeleteUserUseCasePort;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand, User> {

  private final DeleteUserUseCasePort deleteUserUseCasePort;

  @Override
  @Transactional
  public User handle(final DeleteUserCommand command) throws ResourceNotFoundException {
    return deleteUserUseCasePort.execute(command);
  }
}
