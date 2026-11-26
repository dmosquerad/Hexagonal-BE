package com.architecture.hexagonal.application.databuilder.model.command.user;

import com.architecture.hexagonal.application.model.command.user.CreateUserCommand;
import lombok.Builder;

@Builder
public class CreateUserInputTestDataBuilder {

  @Builder.Default private String email = "test@example.com";

  @Builder.Default private String name = "Test User";

  public CreateUserCommand createUserInput() {
    return CreateUserCommand.builder().email(email).name(name).build();
  }
}
