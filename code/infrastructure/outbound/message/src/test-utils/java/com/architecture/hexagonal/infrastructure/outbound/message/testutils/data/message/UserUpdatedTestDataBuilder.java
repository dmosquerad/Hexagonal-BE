package com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message;

import com.architecture.hexagonal.infrastructure.outbound.message.data.UserUpdated;
import lombok.Builder;

@Builder
public class UserUpdatedTestDataBuilder {

  @Builder.Default
  private String userId = "4059510b-ceb3-4d4c-913e-1759acbd62a4";

  @Builder.Default
  private String name = "Test User";

  @Builder.Default
  private String email = "test@example.com";

  public UserUpdated userUpdated() {
    return UserUpdated.builder()
        .userId(userId)
        .name(name)
        .email(email)
        .build();
  }
}
