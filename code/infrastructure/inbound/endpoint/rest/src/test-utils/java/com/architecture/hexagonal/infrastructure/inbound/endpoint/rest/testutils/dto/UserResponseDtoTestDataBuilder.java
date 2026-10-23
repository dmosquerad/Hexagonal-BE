package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.testutils.dto;

import com.architecture.hexagonal.contract.inbound.endpoint.rest.user.server.dto.UserReadDto;
import com.architecture.hexagonal.contract.inbound.endpoint.rest.user.server.dto.UserResponseDto;
import java.time.OffsetDateTime;

import com.architecture.hexagonal.application.testutils.time.TestClock;
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
    final UserResponseDto userResponseDto = new UserResponseDto();
    userResponseDto.setStatus(status);
    userResponseDto.setDate(date);
    userResponseDto.setData(userReadDto);

    return userResponseDto;
  }
}