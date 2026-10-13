package com.architecture.hexagonal.application.usecase.email.getblockedrules.impl;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.architecture.hexagonal.application.port.out.transaction.TransactionReadBoundary;
import com.architecture.hexagonal.application.service.business.email.EmailService;
import com.architecture.hexagonal.contract.application.usecase.generated.email.GetBlockedRulesQuery;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationUseCase
public class GetBlockedRulesUseCaseImpl
    implements QueryHandler<GetBlockedRulesQuery, EmailBlockRulesVo> {

  private final TransactionReadBoundary transactionReadBoundary;
  private final EmailService emailService;

  @Override
  public EmailBlockRulesVo handle(final @NonNull GetBlockedRulesQuery getBlockedRulesQuery) {
    return transactionReadBoundary.execute(emailService::getBlockedRules);
  }
}
