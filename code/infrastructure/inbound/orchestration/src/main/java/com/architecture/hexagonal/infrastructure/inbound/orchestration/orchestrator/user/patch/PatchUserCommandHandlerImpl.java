package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.patch;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserInput;
import com.architecture.hexagonal.application.business.user.patch.usecase.PatchUserUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.PatchUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.PatchUserCommandMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatchUserCommandHandlerImpl implements CommandHandler<PatchUserCommandDto, User> {

  private final TransactionBoundary transactionBoundary;
  private final PatchUserCommandMapper patchUserCommandMapper;
  private final PatchUserUseCase patchUserUseCase;

  @Override
  public User handle(final @NonNull PatchUserCommandDto patchUserCommandDto) {
    final PatchUserInput patchUserInput =
        patchUserCommandMapper.toPatchUserCommand(patchUserCommandDto);
    return transactionBoundary.write(() -> patchUserUseCase.execute(patchUserInput));
  }
}
