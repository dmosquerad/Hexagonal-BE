package com.architecture.hexagonal.application.testutils.data.input.command;

import com.architecture.hexagonal.domain.input.command.CreateUserCommand;
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
