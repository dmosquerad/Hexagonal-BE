package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event;

import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserCreatedEventTestDataBuilder {

  @Builder.Default
  private final UUID userId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

  @Builder.Default private final String name = "Test User";
  @Builder.Default private final String email = "test@example.com";

  public UserCreatedEvent userCreatedEvent() {
    final UserCreatedEvent event = new UserCreatedEvent();
    event.setUserId(userId);
    event.setName(name);
    event.setEmail(email);
    return event;
  }
}
