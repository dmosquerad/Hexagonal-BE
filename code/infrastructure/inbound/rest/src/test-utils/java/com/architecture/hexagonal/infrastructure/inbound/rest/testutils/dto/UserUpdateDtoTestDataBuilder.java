package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto;

import com.architecture.hexagonal.contract.inbound.rest.user.server.dto.UserUpdateDto;
import lombok.Builder;

@Builder
public class UserUpdateDtoTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserUpdateDto userUpdateDto() {
    final UserUpdateDto userUpdateDto = new UserUpdateDto();
    userUpdateDto.setEmail(email);
    userUpdateDto.setName(name);

    return userUpdateDto;
  }
}