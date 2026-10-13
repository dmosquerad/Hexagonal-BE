package com.architecture.hexagonal.application.testutils.orchestration.data.query;

import com.architecture.hexagonal.contract.application.generated.user.FindUserByUserIdQuery;
import lombok.Builder;

import java.util.UUID;

@Builder
public class FindUserByUserIdInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public FindUserByUserIdQuery findUserByUserIdInput() {
    final FindUserByUserIdQuery query = new FindUserByUserIdQuery();
    query.setUserId(userId);
    return query;
  }
}
