package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.testutils.data;

import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserDeletedMessageTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  public UserDeletedMessage userDeletedMessage() {
    final UserDeletedMessage message = new UserDeletedMessage();
    message.setUserId(userId);
    return message;
  }
}
