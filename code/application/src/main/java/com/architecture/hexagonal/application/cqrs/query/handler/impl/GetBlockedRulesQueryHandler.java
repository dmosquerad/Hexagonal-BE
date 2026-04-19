package com.architecture.hexagonal.application.cqrs.query.handler.impl;

import com.architecture.hexagonal.application.cqrs.query.handler.QueryHandler;
import com.architecture.hexagonal.application.cqrs.query.request.GetBlockedRulesQuery;
import com.architecture.hexagonal.application.port.in.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBlockedRulesQueryHandler implements QueryHandler<GetBlockedRulesQuery, EmailBlockRulesVo> {

  private final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort;

  @Override
  public EmailBlockRulesVo handle(final GetBlockedRulesQuery query) {
    return getBlockedRulesUseCasePort.execute();
  }
}
