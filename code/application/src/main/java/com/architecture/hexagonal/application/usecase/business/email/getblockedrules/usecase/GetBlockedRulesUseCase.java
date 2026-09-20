package com.architecture.hexagonal.application.usecase.business.email.getblockedrules.usecase;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;

public interface GetBlockedRulesUseCase {
  EmailBlockRulesVo execute();
}
