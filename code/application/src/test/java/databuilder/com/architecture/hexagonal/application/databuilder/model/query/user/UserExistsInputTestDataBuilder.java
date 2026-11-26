package com.architecture.hexagonal.application.databuilder.model.query.user;

import com.architecture.hexagonal.application.model.query.user.UserExistsQuery;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserExistsInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public UserExistsQuery userExistsInput() {
    return UserExistsQuery.builder().userId(userId).build();
  }
}
