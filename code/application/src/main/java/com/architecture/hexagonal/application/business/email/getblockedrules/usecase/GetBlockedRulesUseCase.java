package com.architecture.hexagonal.application.business.email.getblockedrules.usecase;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;

public interface GetBlockedRulesUseCase {
  EmailBlockRulesVo execute();
}
