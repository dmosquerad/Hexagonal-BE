package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.port.in.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
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
