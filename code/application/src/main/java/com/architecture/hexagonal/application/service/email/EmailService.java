package com.architecture.hexagonal.application.service.email;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import lombok.NonNull;

public interface EmailService {
  EmailBlockRulesVo getBlockedRules();

  boolean isEmailBlocked(@NonNull EmailVo email);
}
