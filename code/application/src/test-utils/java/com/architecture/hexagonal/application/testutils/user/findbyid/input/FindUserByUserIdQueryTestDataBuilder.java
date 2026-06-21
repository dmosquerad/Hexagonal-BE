package com.architecture.hexagonal.application.testutils.user.findbyid.input;

import com.architecture.hexagonal.application.business.user.findbyid.input.FindUserByUserIdQuery;
import java.util.UUID;
import lombok.Builder;

@Builder
public class FindUserByUserIdQueryTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public FindUserByUserIdQuery findUserByUserIdQuery() {
    return FindUserByUserIdQuery.builder()
        .userId(userId)
        .build();
  }
}
