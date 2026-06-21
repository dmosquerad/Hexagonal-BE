package com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query;

import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import lombok.Builder;

@Builder
public class GetBlockedRulesQueryDtoTestDataBuilder {

  public GetBlockedRulesQueryDto getBlockedRulesQueryDto() {
    return new GetBlockedRulesQueryDto();
  }
}
