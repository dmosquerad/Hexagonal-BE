package com.architecture.hexagonal.application.testutils.user.exists.input;

import com.architecture.hexagonal.application.business.user.exists.input.UserExistsQuery;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserExistsQueryTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public UserExistsQuery userExistsQuery() {
    return UserExistsQuery.builder()
        .userId(userId)
        .build();
  }
}
