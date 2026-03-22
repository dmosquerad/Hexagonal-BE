package com.architecture.hexagonal.application.testutils.data.input.query;

import com.architecture.hexagonal.domain.input.query.FindUserByUserIdQuery;
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
