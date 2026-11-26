package com.architecture.hexagonal.application.databuilder.model.query.email;

import com.architecture.hexagonal.application.model.query.email.GetBlockedRulesQuery;
import lombok.Builder;

@Builder
public class GetBlockedRulesQueryTestDataBuilder {

  public GetBlockedRulesQuery getBlockedRulesQuery() {
    return new GetBlockedRulesQuery();
  }
}
