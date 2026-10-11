package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto;

import com.architecture.hexagonal.contract.inbound.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public class EmailBlockRulesResponseDtoTestDataBuilder {

  @Builder.Default
  private OffsetDateTime date = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private Integer status = 200;

  @Builder.Default
  private com.architecture.hexagonal.contract.inbound.rest.email.server.dto.EmailBlockRulesDto data =
      EmailBlockRulesDtoTestDataBuilder.builder().build().emailBlockRulesDto();

  public EmailBlockRulesResponseDto emailBlockRulesResponseDto() {
    final EmailBlockRulesResponseDto response = new EmailBlockRulesResponseDto();
    response.setDate(date);
    response.setStatus(status);
    response.setData(data);

    return response;
  }
}
