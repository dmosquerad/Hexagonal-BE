package com.architecture.hexagonal.application.email.getblockedrules.usecase.impl;

import com.architecture.hexagonal.application.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetBlockedRulesUseCaseImpl implements GetBlockedRulesUseCase {

  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  public EmailBlockRulesVo execute() {
    return emailConfigurationPort.getBlockedRules();
  }
}
