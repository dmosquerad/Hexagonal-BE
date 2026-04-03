package com.architecture.hexagonal.application.port.out;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import java.util.Set;

public interface EmailConfigurationPort {

    Set<EmailVo> filterBlocked(Set<EmailVo> emails);

    Set<EmailVo> filterAllowed(Set<EmailVo> emails);

    EmailBlockRulesVo getBlockedRules();
}
