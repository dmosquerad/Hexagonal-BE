package com.architecture.hexagonal.domain.databuilder.model.event.user;

import com.architecture.hexagonal.domain.model.event.user.UserUpdatedEvent;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserUpdatedEventTestDataBuilder {

  @Builder.Default
  private final UUID userId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

  @Builder.Default private String name = "Test User";
  @Builder.Default private String email = "test@example.com";

  public UserUpdatedEvent userUpdatedEvent() {
    return UserUpdatedEvent.builder().userId(userId).name(name).email(email).build();
  }
}
