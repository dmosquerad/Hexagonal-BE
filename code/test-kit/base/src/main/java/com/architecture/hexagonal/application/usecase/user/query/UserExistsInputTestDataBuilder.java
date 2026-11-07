package com.architecture.hexagonal.application.usecase.user.query;

import com.architecture.hexagonal.contract.bus.model.user.query.UserExistsQuery;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserExistsInputTestDataBuilder {

  @Builder.Default private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public UserExistsQuery userExistsInput() {
    final UserExistsQuery query = new UserExistsQuery();
    query.setUserId(userId);
    return query;
  }
}
