package com.architecture.hexagonal.infrastructure.outbound.database.testutils.data.entity;

import com.architecture.hexagonal.domain.data.User;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public User user() {
    return User.builder()
        .userId(userId)
        .email(email)
        .name(name)
        .build();
  }
}
