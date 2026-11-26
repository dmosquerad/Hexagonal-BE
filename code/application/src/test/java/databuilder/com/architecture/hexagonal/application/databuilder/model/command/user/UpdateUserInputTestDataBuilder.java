package com.architecture.hexagonal.application.databuilder.model.command.user;

import com.architecture.hexagonal.application.model.command.user.UpdateUserCommand;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UpdateUserInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default private String name = "Updated Name";

  @Builder.Default private String email = "updated@example.com";

  public UpdateUserCommand updateUserInput() {
    return UpdateUserCommand.builder().userId(userId).name(name).email(email).build();
  }
}
