package com.architecture.hexagonal.application.port.out;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;

public interface EmailConfigurationPort {

    EmailBlockRulesVo getBlockedRules();
}
