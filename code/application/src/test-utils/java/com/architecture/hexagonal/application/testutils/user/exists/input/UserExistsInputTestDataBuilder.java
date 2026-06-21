package com.architecture.hexagonal.application.testutils.user.exists.input;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsInput;

import java.util.UUID;
import lombok.Builder;

@Builder
public class UserExistsInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public UserExistsInput userExistsInput() {
    return UserExistsInput.builder()
        .userId(userId)
        .build();
  }
}
