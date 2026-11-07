package com.architecture.hexagonal.application.usecase.user.command;

import com.architecture.hexagonal.contract.bus.model.user.command.PatchUserCommand;
import java.util.UUID;
import lombok.Builder;

@Builder
public class PatchUserInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default private String name = "Patched Name";

  @Builder.Default private String email = "patched@example.com";

  public PatchUserCommand patchUserInput() {
    final PatchUserCommand command = new PatchUserCommand();
    command.setUserId(userId);
    command.setName(name);
    command.setEmail(email);
    return command;
  }
}
