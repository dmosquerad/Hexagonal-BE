package com.architecture.hexagonal.application.service.email.impl;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import com.architecture.hexagonal.application.port.out.email.EmailConfigurationPort;
import com.architecture.hexagonal.application.service.email.EmailService;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import com.architecture.hexagonal.domain.service.EmailBlockPolicy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  public EmailBlockRulesVo getBlockedRules() {
    return emailConfigurationPort.getBlockedRules();
  }

  @Override
  public boolean isEmailBlocked(final @NonNull EmailVo email) {
    return EmailBlockPolicy.isBlocked(email, emailConfigurationPort.getBlockedRules());
  }
}
