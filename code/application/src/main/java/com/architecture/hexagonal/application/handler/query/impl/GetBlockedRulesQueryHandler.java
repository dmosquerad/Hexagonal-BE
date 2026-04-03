package com.architecture.hexagonal.application.handler.query.impl;

import com.architecture.hexagonal.application.handler.query.QueryHandler;
import com.architecture.hexagonal.application.input.query.GetBlockedRulesQuery;
import com.architecture.hexagonal.application.port.in.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBlockedRulesQueryHandler implements QueryHandler<GetBlockedRulesQuery, EmailBlockRulesVo> {

  private final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort;

  @Override
  public Class<GetBlockedRulesQuery> getQueryType() {
    return GetBlockedRulesQuery.class;
  }

  @Override
  public EmailBlockRulesVo handle(final GetBlockedRulesQuery query) {
    return getBlockedRulesUseCasePort.execute();
  }
}
