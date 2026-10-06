package com.architecture.hexagonal.application.usecase.business.email.getblockedrules.usecase.impl;

import com.architecture.hexagonal.application.annotation.UseCase;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.application.usecase.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class GetBlockedRulesUseCaseImpl implements GetBlockedRulesUseCase {

  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  public EmailBlockRulesVo execute() {
    return emailConfigurationPort.getBlockedRules();
  }
}
