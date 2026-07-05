package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message;

import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import lombok.Builder;

@Builder
public class UserDeletedTestDataBuilder {

  @Builder.Default
  private String userId = "4059510b-ceb3-4d4c-913e-1759acbd62a4";

  @Builder.Default
  private String name = "Test User";

  @Builder.Default
  private String email = "test@example.com";

  public UserDeleted userDeleted() {
      final UserDeleted userDeleted = new UserDeleted();
      userDeleted.setUserId(userId);
      userDeleted.setName(name);
      userDeleted.setEmail(email);

      return userDeleted;
  }
}
