package com.architecture.hexagonal.application.testutils.user.delete.input;

import com.architecture.hexagonal.application.business.user.delete.input.DeleteUserInput;
import java.util.UUID;
import lombok.Builder;

@Builder
public class DeleteUserInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public DeleteUserInput deleteUserInput() {
    return DeleteUserInput.builder()
        .userId(userId)
        .build();
  }
}
