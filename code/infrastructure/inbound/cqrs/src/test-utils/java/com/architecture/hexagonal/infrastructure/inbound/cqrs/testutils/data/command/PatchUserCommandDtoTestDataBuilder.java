package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.PatchUserCommandDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public class PatchUserCommandDtoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.randomUUID();

  @Builder.Default
  private String name = "Patched Name";

  @Builder.Default
  private String email = "patched@example.com";

  public PatchUserCommandDto patchUserCommandDto() {
    PatchUserCommandDto dto = new PatchUserCommandDto();
    dto.setUserId(userId);
    dto.setName(name);
    dto.setEmail(email);
    return dto;
  }
}
