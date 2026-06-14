package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.delete.command.handler;

import com.architecture.hexagonal.application.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.application.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.DeleteUserCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommandDto, User> {

  private final DeleteUserCommandMapper deleteUserCommandMapper;
  private final DeleteUserUseCase deleteUserUseCase;

  @Override
  @Transactional
  public User handle(final DeleteUserCommandDto deleteUserCommandDto) throws DomainException {
    final DeleteUserCommand deleteUserCommand =
        deleteUserCommandMapper.toDeleteUserCommand(deleteUserCommandDto);
    return deleteUserUseCase.execute(deleteUserCommand);
  }
}
