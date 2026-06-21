package com.architecture.hexagonal.application.testutils.user.findbyid.input;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdInput;
import java.util.UUID;
import lombok.Builder;

@Builder
public class FindUserByUserIdInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public FindUserByUserIdInput findUserByUserIdInput() {
    return FindUserByUserIdInput.builder()
        .userId(userId)
        .build();
  }
}
