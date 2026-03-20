package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.ResponseErrorDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public class ResponseErrorDtoTestDataBuilder {

  @Builder.Default
  private OffsetDateTime date = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private Integer status = 404;

  @Builder.Default
  private String title = "Not Found";

  @Builder.Default
  private String detail = "Error detail";

  public ResponseErrorDto responseErrorDto() {
    ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(date);
    responseErrorDto.setStatus(status);
    responseErrorDto.setTitle(title);
    responseErrorDto.setDetail(detail);

    return responseErrorDto;
  }
}
