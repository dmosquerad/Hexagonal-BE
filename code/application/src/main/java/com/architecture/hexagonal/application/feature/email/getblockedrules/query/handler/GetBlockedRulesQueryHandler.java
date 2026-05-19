package com.architecture.hexagonal.application.feature.email.getblockedrules.query.handler;

import com.architecture.hexagonal.application.feature.email.getblockedrules.query.GetBlockedRulesQuery;
import com.architecture.hexagonal.application.common.cqrs.query.QueryHandler;
import com.architecture.hexagonal.application.feature.email.getblockedrules.port.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetBlockedRulesQueryHandler implements QueryHandler<GetBlockedRulesQuery, EmailBlockRulesVo> {

  private final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort;

  @Override
  @Transactional(readOnly = true)
  public EmailBlockRulesVo handle(final GetBlockedRulesQuery query) {
    return getBlockedRulesUseCasePort.execute();
  }
}
