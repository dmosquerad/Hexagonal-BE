package com.architecture.hexagonal.application.port.out.email;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;

public interface EmailConfigurationPort {

  EmailBlockRulesVo getBlockedRules();
}
