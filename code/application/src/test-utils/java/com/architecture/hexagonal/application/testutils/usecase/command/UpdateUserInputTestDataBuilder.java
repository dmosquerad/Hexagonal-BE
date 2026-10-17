package com.architecture.hexagonal.application.testutils.usecase.command;

import com.architecture.hexagonal.contract.bus.model.user.UpdateUserCommand;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UpdateUserInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String name = "Updated Name";

  @Builder.Default
  private String email = "updated@example.com";

  public UpdateUserCommand updateUserInput() {
    final UpdateUserCommand command = new UpdateUserCommand();
    command.setUserId(userId);
    command.setName(name);
    command.setEmail(email);
    return command;
  }
}
