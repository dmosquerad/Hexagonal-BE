package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.email.getblockedrules;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBlockedRulesQueryHandlerImpl
    implements QueryHandler<GetBlockedRulesQueryDto, EmailBlockRulesVo> {

  private final TransactionBoundary transactionBoundary;
  private final GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Override
  public EmailBlockRulesVo handle(final GetBlockedRulesQueryDto getBlockedRulesQueryDto) {
    return transactionBoundary.read(getBlockedRulesUseCase::execute);
  }
}
