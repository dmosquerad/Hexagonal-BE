package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.update;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserInput;
import com.architecture.hexagonal.application.business.user.update.usecase.UpdateUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UpdateUserCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandlerImpl implements CommandHandler<UpdateUserCommandDto, User> {

  private final TransactionBoundary transactionBoundary;
  private final UpdateUserCommandMapper updateUserCommandMapper;
  private final UpdateUserUseCase updateUserUseCase;

  @Override
  public User handle(final UpdateUserCommandDto updateUserCommandDto) {
    final UpdateUserInput updateUserInput =
        updateUserCommandMapper.toUpdateUserCommand(updateUserCommandDto);
    return transactionBoundary.write(() -> updateUserUseCase.execute(updateUserInput));
  }
}
