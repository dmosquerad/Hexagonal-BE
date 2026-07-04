package com.architecture.hexagonal.application.port.database.query;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserQuery {

  String host;
  Boolean blockEmail;
  EmailBlockRulesVo blockedRules;
}
