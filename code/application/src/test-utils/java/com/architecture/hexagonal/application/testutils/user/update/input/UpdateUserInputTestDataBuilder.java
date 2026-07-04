package com.architecture.hexagonal.application.testutils.user.update.input;

import com.architecture.hexagonal.application.business.user.update.input.UpdateUserInput;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UpdateUserInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UpdateUserInput updateUserInput() {
    return UpdateUserInput.builder()
        .userId(userId)
        .email(email)
        .name(name)
        .build();
  }
}