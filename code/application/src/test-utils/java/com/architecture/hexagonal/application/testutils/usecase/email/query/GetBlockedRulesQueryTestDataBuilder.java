package com.architecture.hexagonal.application.testutils.usecase.email.query;

import com.architecture.hexagonal.contract.bus.model.email.query.GetBlockedRulesQuery;
import lombok.Builder;

@Builder
public class GetBlockedRulesQueryTestDataBuilder {

  public GetBlockedRulesQuery getBlockedRulesQuery() {
    return new GetBlockedRulesQuery();
  }
}
