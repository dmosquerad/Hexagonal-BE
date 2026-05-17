package com.architecture.hexagonal.application.feature.email.getblockedrules.usecase;

import com.architecture.hexagonal.application.feature.email.getblockedrules.port.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.application.port.configuration.EmailConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetBlockedRulesUseCase implements GetBlockedRulesUseCasePort {

  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  @Transactional(readOnly = true)
  public EmailBlockRulesVo execute() {
    return emailConfigurationPort.getBlockedRules();
  }
}
