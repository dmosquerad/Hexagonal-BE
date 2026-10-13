package com.architecture.hexagonal.application.testutils.orchestration.data.query;

import com.architecture.hexagonal.contract.application.generated.email.GetBlockedRulesQuery;
import lombok.Builder;

@Builder
public class GetBlockedRulesQueryTestDataBuilder {

  public GetBlockedRulesQuery getBlockedRulesQuery() {
    return new GetBlockedRulesQuery();
  }
}
