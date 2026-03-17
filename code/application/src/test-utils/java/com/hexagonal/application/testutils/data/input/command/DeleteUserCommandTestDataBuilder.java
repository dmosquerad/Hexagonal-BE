package com.hexagonal.application.testutils.data.input.command;

import com.architecture.hexagonal.domain.input.command.DeleteUserCommand;
import java.util.UUID;
import lombok.Builder;

@Builder
public class DeleteUserCommandTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public DeleteUserCommand deleteUserCommand() {
    return DeleteUserCommand.builder()
        .userId(userId)
        .build();
  }
}
