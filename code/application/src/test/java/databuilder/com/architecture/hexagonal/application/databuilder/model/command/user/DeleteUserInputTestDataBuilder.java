package com.architecture.hexagonal.application.databuilder.model.command.user;

import com.architecture.hexagonal.application.model.command.user.DeleteUserCommand;
import java.util.UUID;
import lombok.Builder;

@Builder
public class DeleteUserInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public DeleteUserCommand deleteUserInput() {
    return DeleteUserCommand.builder().userId(userId).build();
  }
}
