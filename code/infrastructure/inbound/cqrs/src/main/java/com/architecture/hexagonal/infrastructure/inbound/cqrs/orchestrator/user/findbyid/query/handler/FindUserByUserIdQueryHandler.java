package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.findbyid.query.handler;

import com.architecture.hexagonal.application.user.findbyid.input.FindUserByUserIdQuery;
import com.architecture.hexagonal.application.user.findbyid.usecase.FindUserByUserIdUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.FindUserByUserIdQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.user.FindUserByUserIdQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FindUserByUserIdQueryHandler implements QueryHandler<FindUserByUserIdQueryDto, User> {

  private final FindUserByUserIdQueryMapper findUserByUserIdQueryMapper;
  private final FindUserByUserIdUseCase findUserByUserIdUseCase;

  @Override
  @Transactional(readOnly = true)
  public User handle(final FindUserByUserIdQueryDto findUserByUserIdQueryDto)
      throws DomainException {
    final FindUserByUserIdQuery findUserByUserIdQuery =
        findUserByUserIdQueryMapper.toFindUserByUserIdQuery(findUserByUserIdQueryDto);
    return findUserByUserIdUseCase.execute(findUserByUserIdQuery);
  }
}
