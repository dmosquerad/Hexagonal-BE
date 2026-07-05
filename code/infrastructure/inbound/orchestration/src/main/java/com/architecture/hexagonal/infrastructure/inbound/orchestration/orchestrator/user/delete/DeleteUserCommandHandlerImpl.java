package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.delete;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import com.architecture.hexagonal.application.business.user.delete.usecase.DeleteUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.DeleteUserCommandMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserCommandHandlerImpl implements CommandHandler<DeleteUserCommandDto, User> {

  private final TransactionBoundary transactionBoundary;
  private final DeleteUserCommandMapper deleteUserCommandMapper;
  private final DeleteUserUseCase deleteUserUseCase;

  @Override
  public User handle(final @NonNull DeleteUserCommandDto deleteUserCommandDto) {
    final DeleteUserInput deleteUserInput =
        deleteUserCommandMapper.toDeleteUserCommand(deleteUserCommandDto);
    return transactionBoundary.write(() -> deleteUserUseCase.execute(deleteUserInput));
  }
}
