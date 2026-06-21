package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.create;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import com.architecture.hexagonal.application.business.user.create.usecase.CreateUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.CreateUserCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserCommandHandlerImpl implements CommandHandler<CreateUserCommandDto, User> {

  private final TransactionBoundary transactionBoundary;
  private final CreateUserCommandMapper createUserCommandMapper;
  private final CreateUserUseCase createUserUseCase;

  @Override
  public User handle(final CreateUserCommandDto createUserCommandDto) {
    final CreateUserInput createUserInput =
        createUserCommandMapper.toCreateUserCommand(createUserCommandDto);
    return transactionBoundary.write(() -> createUserUseCase.execute(createUserInput));
  }
}
