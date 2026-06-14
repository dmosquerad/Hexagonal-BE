package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.getallfiltered.query.handler;

import com.architecture.hexagonal.application.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.application.user.getall.input.GetUsersQuery;
import com.architecture.hexagonal.application.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.pagination.PaginationMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetUsersFilteredQueryHandler
    implements QueryHandler<GetUsersFilteredQueryDto, PaginationResult<User>> {

  private final GetAllUsersUseCase getAllUsersUseCase;
  private final GetBlockedRulesUseCase getBlockedRulesUseCase;
  private final PaginationMapper paginationMapper;

  @Override
  @Transactional(readOnly = true)
  public PaginationResult<User> handle(final GetUsersFilteredQueryDto getUsersFilteredQueryDto)
      throws DomainException {
    final GetUsersQuery filteredQuery =
        GetUsersQuery.builder()
            .host(getUsersFilteredQueryDto.getHost())
            .blockEmail(getUsersFilteredQueryDto.getBlockEmail())
            .pagination(paginationMapper.toPagination(getUsersFilteredQueryDto.getPagination()))
            .blockedRules(
                Objects.nonNull(getUsersFilteredQueryDto.getBlockEmail())
                    ? getBlockedRulesUseCase.execute()
                    : null)
            .build();
    return getAllUsersUseCase.execute(filteredQuery);
  }
}
