package com.architecture.hexagonal.domain.port.out.configuration;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;

public interface EmailConfigurationPort {

  EmailBlockRulesVo getBlockedRules();
}
