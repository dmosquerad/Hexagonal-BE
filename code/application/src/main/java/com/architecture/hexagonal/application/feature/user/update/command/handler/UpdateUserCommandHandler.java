package com.architecture.hexagonal.application.feature.user.update.command.handler;

import com.architecture.hexagonal.application.feature.user.update.command.UpdateUserCommand;
import com.architecture.hexagonal.application.common.cqrs.command.CommandHandler;
import com.architecture.hexagonal.application.feature.user.update.port.UpdateUserUseCasePort;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, User> {

  private final UpdateUserUseCasePort updateUserUseCasePort;

  @Override
  @Transactional
  public User handle(final UpdateUserCommand command) throws ResourceNotFoundException, InvalidValueException {
    return updateUserUseCasePort.execute(command);
  }
}
