package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto;

import com.architecture.hexagonal.contract.inbound.rest.user.server.dto.ResponseErrorDto;
import java.time.OffsetDateTime;

import com.architecture.hexagonal.application.testutils.time.TestClock;
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
    final ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(date);
    responseErrorDto.setStatus(status);
    responseErrorDto.setTitle(title);
    responseErrorDto.setDetail(detail);

    return responseErrorDto;
  }
}
