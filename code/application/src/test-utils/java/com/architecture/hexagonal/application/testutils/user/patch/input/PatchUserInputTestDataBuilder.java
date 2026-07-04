package com.architecture.hexagonal.application.testutils.user.patch.input;

import com.architecture.hexagonal.application.business.user.patch.input.PatchUserInput;
import java.util.UUID;

import lombok.Builder;

@Builder
public class PatchUserInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public PatchUserInput patchUserInput() {
    return PatchUserInput.builder()
        .userId(userId)
        .email(email)
        .name(name)
        .build();
  }
}