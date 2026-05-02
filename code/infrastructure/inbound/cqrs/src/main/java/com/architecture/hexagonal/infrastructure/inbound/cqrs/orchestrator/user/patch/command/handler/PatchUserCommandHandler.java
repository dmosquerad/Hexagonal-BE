package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.patch.command.handler;

import com.architecture.hexagonal.application.user.patch.input.PatchUserCommand;
import com.architecture.hexagonal.application.user.patch.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.PatchUserCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PatchUserCommandHandler implements CommandHandler<PatchUserCommandDto, User> {

  private final PatchUserCommandMapper patchUserCommandMapper;
  private final PatchUserUseCase patchUserUseCase;

  @Override
  @Transactional
  public User handle(final PatchUserCommandDto patchUserCommandDto) throws DomainException {
    final PatchUserCommand patchUserCommand =
        patchUserCommandMapper.toPatchUserCommand(patchUserCommandDto);
    return patchUserUseCase.execute(patchUserCommand);
  }
}
