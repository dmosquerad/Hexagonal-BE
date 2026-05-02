package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.exists.query.handler;

import com.architecture.hexagonal.application.user.exists.input.UserExistsQuery;
import com.architecture.hexagonal.application.user.exists.usecase.UserExistsUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UserExistsQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.UserExistsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserExistsQueryHandler implements QueryHandler<UserExistsQueryDto, Void> {

  private final UserExistsQueryMapper userExistsQueryMapper;
  private final UserExistsUseCase userExistsUseCase;

  @Override
  @Transactional(readOnly = true)
  public Void handle(final UserExistsQueryDto userExistsQueryDto) throws DomainException {
    final UserExistsQuery userExistsQuery =
        userExistsQueryMapper.toUserExistsQuery(userExistsQueryDto);
    userExistsUseCase.execute(userExistsQuery);
    return null;
  }
}
