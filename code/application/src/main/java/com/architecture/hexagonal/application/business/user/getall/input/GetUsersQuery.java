package com.architecture.hexagonal.application.business.user.getall.input;

import com.architecture.hexagonal.domain.model.pagination.Pagination;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetUsersQuery {
  String host;
  Boolean blockEmail;
  Pagination pagination;
  EmailBlockRulesVo blockedRules;
}
