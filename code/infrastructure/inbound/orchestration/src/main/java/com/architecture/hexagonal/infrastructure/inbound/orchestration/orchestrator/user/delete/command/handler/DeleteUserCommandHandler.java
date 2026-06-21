package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.delete.command.handler;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserCommand;
import com.architecture.hexagonal.application.business.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.DeleteUserCommandMapper;
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
