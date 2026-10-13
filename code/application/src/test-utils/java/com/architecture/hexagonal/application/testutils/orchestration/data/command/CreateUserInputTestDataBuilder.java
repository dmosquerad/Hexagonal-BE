package com.architecture.hexagonal.application.testutils.orchestration.data.command;

import com.architecture.hexagonal.contract.application.generated.user.CreateUserCommand;
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
