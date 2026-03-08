package com.hexagonal.application.testutils.data.entity;

import com.architecture.hexagonal.domain.data.UserDo;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserDoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserDo userDo() {
    return UserDo.builder()
        .userId(userId)
        .email(email)
        .name(name)
        .build();
  }
}
