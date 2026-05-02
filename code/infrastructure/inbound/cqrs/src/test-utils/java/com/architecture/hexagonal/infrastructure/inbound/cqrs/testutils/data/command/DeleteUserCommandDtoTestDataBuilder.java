package com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.command;

import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.DeleteUserCommandDto;
import lombok.Builder;

import java.util.UUID;

@Builder
public class DeleteUserCommandDtoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.randomUUID();

  public DeleteUserCommandDto deleteUserCommandDto() {
    DeleteUserCommandDto dto = new DeleteUserCommandDto();
    dto.setUserId(userId);
    return dto;
  }
}
