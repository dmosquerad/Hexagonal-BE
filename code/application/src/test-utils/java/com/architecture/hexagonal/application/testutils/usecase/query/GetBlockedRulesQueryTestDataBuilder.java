package com.architecture.hexagonal.application.testutils.usecase.query;

import com.architecture.hexagonal.contract.bus.model.email.GetBlockedRulesQuery;
import lombok.Builder;

@Builder
public class GetBlockedRulesQueryTestDataBuilder {

  public GetBlockedRulesQuery getBlockedRulesQuery() {
    return new GetBlockedRulesQuery();
  }
}
