package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.create.command.handler;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserCommand;
import com.architecture.hexagonal.application.business.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.CreateUserCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommandDto, User> {

  private final CreateUserCommandMapper createUserCommandMapper;
  private final CreateUserUseCase createUserUseCase;

  @Override
  @Transactional
  public User handle(final CreateUserCommandDto createUserCommandDto) throws DomainException {
    final CreateUserCommand createUserCommand =
        createUserCommandMapper.toCreateUserCommand(createUserCommandDto);
    return createUserUseCase.execute(createUserCommand);
  }
}
