package com.architecture.hexagonal.application.testutils.user.create.input;

import com.architecture.hexagonal.application.business.user.create.input.CreateUserInput;
import lombok.Builder;

@Builder
public class CreateUserInputTestDataBuilder {

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public CreateUserInput createUserInput() {
    return CreateUserInput.builder()
        .email(email)
        .name(name)
        .build();
  }
}
