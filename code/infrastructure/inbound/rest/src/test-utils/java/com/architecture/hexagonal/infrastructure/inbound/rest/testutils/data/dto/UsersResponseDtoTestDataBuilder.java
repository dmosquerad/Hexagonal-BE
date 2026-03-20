package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserReadDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UsersResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import lombok.Builder;

@Builder
public class UsersResponseDtoTestDataBuilder {

  @Builder.Default
  private OffsetDateTime date = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private Integer status = 200;

  @Builder.Default
  private Set<UserReadDto> userReadDto = Collections.singleton(UserReadDtoTestDataBuilder
      .builder()
      .build()
      .userReadDto());

  public UsersResponseDto usersResponseDto() {
    UsersResponseDto usersResponseDto = new UsersResponseDto();
    usersResponseDto.setStatus(status);
    usersResponseDto.setDate(this.date);
    usersResponseDto.setData(this.userReadDto);

    return usersResponseDto;
  }
}
