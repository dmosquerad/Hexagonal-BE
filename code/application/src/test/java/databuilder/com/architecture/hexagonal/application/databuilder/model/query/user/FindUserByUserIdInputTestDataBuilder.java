package com.architecture.hexagonal.application.databuilder.model.query.user;

import com.architecture.hexagonal.application.model.query.user.FindUserByUserIdQuery;
import java.util.UUID;
import lombok.Builder;

@Builder
public class FindUserByUserIdInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public FindUserByUserIdQuery findUserByUserIdInput() {
    return FindUserByUserIdQuery.builder().userId(userId).build();
  }
}
