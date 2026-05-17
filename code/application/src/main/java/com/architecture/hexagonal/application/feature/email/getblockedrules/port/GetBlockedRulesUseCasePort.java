package com.architecture.hexagonal.application.feature.email.getblockedrules.port;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GetBlockedRulesUseCasePort {
  EmailBlockRulesVo execute();
}
