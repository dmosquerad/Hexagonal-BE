package com.architecture.hexagonal.application.feature.user.getall.query;

import com.architecture.hexagonal.application.common.pagination.Pagination;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAllUserQueryFiltered {
  String host;
  Boolean blockEmail;
  Pagination pagination;
  EmailBlockRulesVo blockedRules;
}
