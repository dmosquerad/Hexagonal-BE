package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data;

import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import lombok.Builder;

@Builder
public class UserCreatedMessageTestDataBuilder {

  @Builder.Default
  private String name = "Test User";

  @Builder.Default
  private String email = "test@example.com";

  public UserCreatedMessage userCreatedMessage() {
    final UserCreatedMessage message = new UserCreatedMessage();
    message.setName(name);
    message.setEmail(email);
    return message;
  }
}
