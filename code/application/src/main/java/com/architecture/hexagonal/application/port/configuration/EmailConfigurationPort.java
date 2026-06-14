package com.architecture.hexagonal.application.port.configuration;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;

public interface EmailConfigurationPort {

  EmailBlockRulesVo getBlockedRules();
}
