package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.query;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.email.GetBlockedRulesQueryDto;
import lombok.Builder;

@Builder
public class GetBlockedRulesQueryDtoTestDataBuilder {

  public GetBlockedRulesQueryDto getBlockedRulesQueryDto() {
    return new GetBlockedRulesQueryDto();
  }
}
