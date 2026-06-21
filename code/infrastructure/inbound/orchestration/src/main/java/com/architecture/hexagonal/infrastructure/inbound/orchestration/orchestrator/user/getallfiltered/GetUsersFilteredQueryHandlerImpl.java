package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.getallfiltered;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.application.business.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.pagination.PaginationMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUsersFilteredQueryHandlerImpl
    implements QueryHandler<GetUsersFilteredQueryDto, PaginationResult<User>> {

  private final TransactionBoundary transactionBoundary;
  private final GetAllUsersUseCase getAllUsersUseCase;
  private final GetBlockedRulesUseCase getBlockedRulesUseCase;
  private final PaginationMapper paginationMapper;

  @Override
  public PaginationResult<User> handle(final GetUsersFilteredQueryDto getUsersFilteredQueryDto) {
    final Pagination pagination =
        paginationMapper.toPagination(getUsersFilteredQueryDto.getPagination());
    final String host = getUsersFilteredQueryDto.getHost();
    final Boolean blockEmail = getUsersFilteredQueryDto.getBlockEmail();

    if (BooleanUtils.isTrue(blockEmail) || BooleanUtils.isFalse(blockEmail)) {
      final GetUsersInput getUsersInput =
          GetUsersInput.builder()
              .host(host)
              .blockEmail(blockEmail)
              .pagination(pagination)
              .blockedRules(transactionBoundary.read(getBlockedRulesUseCase::execute))
              .build();

      return transactionBoundary.read(() -> getAllUsersUseCase.execute(getUsersInput));
    }

    final GetUsersInput getUsersInput =
        GetUsersInput.builder().host(host).blockEmail(blockEmail).pagination(pagination).build();

    return transactionBoundary.read(() -> getAllUsersUseCase.execute(getUsersInput));
  }
}
