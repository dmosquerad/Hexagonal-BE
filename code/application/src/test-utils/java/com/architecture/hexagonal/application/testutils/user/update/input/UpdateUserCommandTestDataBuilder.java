package com.architecture.hexagonal.application.testutils.user.update.input;

import com.architecture.hexagonal.application.user.update.input.UpdateUserCommand;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UpdateUserCommandTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UpdateUserCommand updateUserCommand() {
    return UpdateUserCommand.builder()
        .userId(userId)
        .email(email)
        .name(name)
        .build();
  }
}