package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.UpdateUserCommandDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UpdateUserCommandDtoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.randomUUID();

  @Builder.Default
  private String name = "Updated Name";

  @Builder.Default
  private String email = "updated@example.com";

  public UpdateUserCommandDto updateUserCommandDto() {
    UpdateUserCommandDto dto = new UpdateUserCommandDto();
    dto.setUserId(userId);
    dto.setName(name);
    dto.setEmail(email);
    return dto;
  }
}
