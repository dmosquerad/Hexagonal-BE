package com.architecture.hexagonal.application.testutils.usecase.query;

import com.architecture.hexagonal.contract.application.usecase.generated.user.UserExistsQuery;
import lombok.Builder;

import java.util.UUID;

@Builder
public class UserExistsInputTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public UserExistsQuery userExistsInput() {
    final UserExistsQuery query = new UserExistsQuery();
    query.setUserId(userId);
    return query;
  }
}
