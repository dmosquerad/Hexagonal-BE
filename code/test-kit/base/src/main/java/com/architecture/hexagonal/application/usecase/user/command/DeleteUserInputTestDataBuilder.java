package com.architecture.hexagonal.application.usecase.user.command;

import com.architecture.hexagonal.contract.bus.model.user.command.DeleteUserCommand;
import java.util.UUID;
import lombok.Builder;

@Builder
public class DeleteUserInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public DeleteUserCommand deleteUserInput() {
    final DeleteUserCommand command = new DeleteUserCommand();
    command.setUserId(userId);
    return command;
  }
}
