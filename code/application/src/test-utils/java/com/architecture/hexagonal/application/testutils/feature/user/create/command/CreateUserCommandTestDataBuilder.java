package com.architecture.hexagonal.application.testutils.feature.user.create.command;

import com.architecture.hexagonal.application.feature.user.create.command.CreateUserCommand;
import lombok.Builder;

@Builder
public class CreateUserCommandTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public CreateUserCommand createUserCommand() {
    return CreateUserCommand.builder()
        .email(email)
        .name(name)
        .build();
  }
}
