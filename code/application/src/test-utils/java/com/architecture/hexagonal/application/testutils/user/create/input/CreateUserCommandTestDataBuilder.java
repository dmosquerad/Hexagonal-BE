package com.architecture.hexagonal.application.testutils.user.create.input;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserCommand;
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
