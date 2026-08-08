package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.exists;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;
import com.architecture.hexagonal.application.business.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.user.UserExistsQueryMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistsQueryHandlerImpl implements QueryHandler<UserExistsQueryDto, Void> {

  private final TransactionBoundary transactionBoundary;
  private final UserExistsQueryMapper userExistsQueryMapper;
  private final UserExistsUseCase userExistsUseCase;

  @Override
  public Void handle(final @NonNull UserExistsQueryDto userExistsQueryDto) {
    final UserExistsInput userExistsInput =
        userExistsQueryMapper.toUserExistsQuery(userExistsQueryDto);

    return transactionBoundary.read(
        () -> {
          userExistsUseCase.execute(userExistsInput);
          return null;
        });
  }
}
