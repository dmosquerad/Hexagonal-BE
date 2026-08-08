package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message;

import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import lombok.Builder;

@Builder
public class UserUpdatedTestDataBuilder {

  @Builder.Default
  private String userId = "4059510b-ceb3-4d4c-913e-1759acbd62a4";

  @Builder.Default
  private String name = "Test User";

  @Builder.Default
  private String email = "test@example.com";

  public UserUpdated userUpdated() {
      final UserUpdated userUpdated = new UserUpdated();
      userUpdated.setUserId(userId);
      userUpdated.setName(name);
      userUpdated.setEmail(email);

      return userUpdated;
  }
}
