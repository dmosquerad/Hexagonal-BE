package com.architecture.hexagonal.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.inbound.rest.dto.ResponseErrorDto;
import com.architecture.hexagonal.inbound.rest.testutils.time.TestClock;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public class ResponseErrorDtoTestDataBuilder {

  @Builder.Default
  private OffsetDateTime date = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private Integer status = 400;

  @Builder.Default
  private List<String> messages = List.of("Error messages");

  public ResponseErrorDto responseErrorDto() {

    ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(date);
    responseErrorDto.status(status);
    responseErrorDto.messages(messages);

    return responseErrorDto;
  }
}
