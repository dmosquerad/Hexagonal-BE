package com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserReadDto;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserReadDtoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserReadDto userReadDto() {
    UserReadDto userReadDto = new UserReadDto();
    userReadDto.setUserId(userId);
    userReadDto.setEmail(email);
    userReadDto.setName(name);

    return userReadDto;
  }
}
