package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.CreateUserCommandDto;
import lombok.Builder;

@Builder
public class CreateUserCommandDtoTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public CreateUserCommandDto createUserCommandDto() {
    CreateUserCommandDto dto = new CreateUserCommandDto();
    dto.setEmail(email);
    dto.setName(name);
    return dto;
  }
}
