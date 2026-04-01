package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.EmailBlockRulesResponseDto;
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
  private com.architecture.hexagonal.infrastructure.inbound.rest.dto.EmailBlockRulesDto data =
      EmailBlockRulesDtoTestDataBuilder.builder().build().emailBlockRulesDto();

  public EmailBlockRulesResponseDto emailBlockRulesResponseDto() {
    EmailBlockRulesResponseDto response = new EmailBlockRulesResponseDto();
    response.setDate(this.date);
    response.setStatus(this.status);
    response.setData(this.data);

    return response;
  }
}
