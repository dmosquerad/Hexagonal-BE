package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data.message;

import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserUpdatedMessageTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String name = "Updated User";

  @Builder.Default
  private String email = "updated@example.com";

  public UserUpdatedMessage userUpdatedMessage() {
    final UserUpdatedMessage message = new UserUpdatedMessage();
    message.setUserId(userId);
    message.setName(name);
    message.setEmail(email);
    return message;
  }
}
