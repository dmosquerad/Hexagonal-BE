package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.event;

import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserUpdatedEventTestDataBuilder {

  @Builder.Default
  private final UUID userId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

  @Builder.Default private final String name = "Test User";
  @Builder.Default private final String email = "test@example.com";

  public UserUpdatedEvent userUpdatedEvent() {
    final UserUpdatedEvent event = new UserUpdatedEvent();
    event.setUserId(userId);
    event.setName(name);
    event.setEmail(email);
    return event;
  }
}
