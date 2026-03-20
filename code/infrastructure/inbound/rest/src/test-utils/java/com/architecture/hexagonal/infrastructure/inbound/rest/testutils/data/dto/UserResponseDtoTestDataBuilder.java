package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserReadDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public class UserResponseDtoTestDataBuilder {

  @Builder.Default
  private OffsetDateTime date = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private Integer status = 200;

  @Builder.Default
  private UserReadDto userReadDto = UserReadDtoTestDataBuilder.builder().build().userReadDto();

  public UserResponseDto userResponseDto() {
    UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setStatus(status);
    userResponseDto.setDate(this.date);
    userResponseDto.setData(this.userReadDto);

    return userResponseDto;
  }
}