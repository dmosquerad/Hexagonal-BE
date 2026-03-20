package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserPatchDto;
import lombok.Builder;

@Builder
public class UserPatchDtoTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserPatchDto userPatchDto() {
    UserPatchDto userPatchDto = new UserPatchDto();
    userPatchDto.setEmail(email);
    userPatchDto.setName(name);

    return userPatchDto;
  }
}