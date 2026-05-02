package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.update.command.handler;

import com.architecture.hexagonal.application.user.update.input.UpdateUserCommand;
import com.architecture.hexagonal.application.user.update.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.UpdateUserCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommandDto, User> {

  private final UpdateUserCommandMapper updateUserCommandMapper;
  private final UpdateUserUseCase updateUserUseCase;

  @Override
  @Transactional
  public User handle(final UpdateUserCommandDto updateUserCommandDto) throws DomainException {
    final UpdateUserCommand updateUserCommand =
        updateUserCommandMapper.toUpdateUserCommand(updateUserCommandDto);
    return updateUserUseCase.execute(updateUserCommand);
  }
}
