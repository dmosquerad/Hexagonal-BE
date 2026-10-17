package com.architecture.hexagonal.application.testutils.usecase.command;

import com.architecture.hexagonal.contract.bus.model.user.DeleteUserCommand;
import lombok.Builder;

import java.util.UUID;

@Builder
public class DeleteUserInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public DeleteUserCommand deleteUserInput() {
    final DeleteUserCommand command = new DeleteUserCommand();
    command.setUserId(userId);
    return command;
  }
}
