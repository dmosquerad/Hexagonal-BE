package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserCreateDto;
import lombok.Builder;

@Builder
public class UserCreateDtoTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserCreateDto userCreateDto() {

    UserCreateDto userCreateDto = new UserCreateDto();
    userCreateDto.setEmail(email);
    userCreateDto.setName(name);

    return userCreateDto;
  }
}