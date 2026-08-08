package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.findbyid;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.FindUserByUserIdQueryMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserByUserIdQueryHandlerImpl
    implements QueryHandler<FindUserByUserIdQueryDto, User> {

  private final TransactionBoundary transactionBoundary;
  private final FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;
  private final FindUserByUserIdUseCase findUserByUserIdUseCase;

  @Override
  public User handle(final @NonNull FindUserByUserIdQueryDto findUserByUserIdQueryDto) {
    final FindUserByUserIdInput findUserByUserIdInput =
        findUserByUserIdQueryMapper.toFindUserByUserIdQuery(findUserByUserIdQueryDto);

    return transactionBoundary.read(() -> findUserByUserIdUseCase.execute(findUserByUserIdInput));
  }
}
