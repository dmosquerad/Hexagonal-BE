package com.architecture.hexagonal.application.feature.user.patch.command.handler;

import com.architecture.hexagonal.application.feature.user.patch.command.PatchUserCommand;
import com.architecture.hexagonal.application.common.cqrs.command.CommandHandler;
import com.architecture.hexagonal.application.feature.user.patch.port.PatchUserUseCasePort;
import com.architecture.hexagonal.domain.exception.InvalidValueException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PatchUserCommandHandler implements CommandHandler<PatchUserCommand, User> {

  private final PatchUserUseCasePort patchUserUseCasePort;

  @Override
  @Transactional
  public User handle(final PatchUserCommand command) throws ResourceNotFoundException, InvalidValueException {
    return patchUserUseCasePort.execute(command);
  }
}
