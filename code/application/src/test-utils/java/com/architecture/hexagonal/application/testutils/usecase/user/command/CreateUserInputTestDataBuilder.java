package com.architecture.hexagonal.application.testutils.usecase.user.command;

import com.architecture.hexagonal.contract.bus.model.user.command.CreateUserCommand;
import lombok.Builder;

@Builder
public class CreateUserInputTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public CreateUserCommand createUserInput() {
    final CreateUserCommand command = new CreateUserCommand();
    command.setEmail(email);
    command.setName(name);
    return command;
  }
}
