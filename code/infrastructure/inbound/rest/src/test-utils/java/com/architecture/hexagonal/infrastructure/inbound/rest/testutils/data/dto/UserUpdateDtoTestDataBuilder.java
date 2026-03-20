package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserUpdateDto;
import lombok.Builder;

@Builder
public class UserUpdateDtoTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserUpdateDto userUpdateDto() {
    UserUpdateDto userUpdateDto = new UserUpdateDto();
    userUpdateDto.setEmail(email);
    userUpdateDto.setName(name);

    return userUpdateDto;
  }
}