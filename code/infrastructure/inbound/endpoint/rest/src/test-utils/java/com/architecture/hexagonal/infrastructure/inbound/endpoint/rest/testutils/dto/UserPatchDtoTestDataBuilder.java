package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.testutils.dto;

import com.architecture.hexagonal.contract.inbound.endpoint.rest.user.server.dto.UserPatchDto;
import lombok.Builder;

@Builder
public class UserPatchDtoTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserPatchDto userPatchDto() {
    final UserPatchDto userPatchDto = new UserPatchDto();
    userPatchDto.setEmail(email);
    userPatchDto.setName(name);

    return userPatchDto;
  }
}