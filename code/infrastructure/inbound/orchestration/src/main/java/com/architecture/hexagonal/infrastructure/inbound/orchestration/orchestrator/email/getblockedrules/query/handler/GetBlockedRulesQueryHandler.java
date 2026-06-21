package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.email.getblockedrules.query.handler;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetBlockedRulesQueryHandler
    implements QueryHandler<GetBlockedRulesQueryDto, EmailBlockRulesVo> {

  private final GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Override
  @Transactional(readOnly = true)
  public EmailBlockRulesVo handle(final GetBlockedRulesQueryDto getBlockedRulesQueryDto) {
    return getBlockedRulesUseCase.execute();
  }
}
