package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedData;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserUpdatedDataTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("2b1f8f0e-6c3a-4f7e-9a53-0d6a6b0f1c11");

  @Builder.Default private String name = "Updated User";
  @Builder.Default private String email = "updated@example.com";

  public UserUpdatedData userUpdatedData() {
    final UserUpdatedData data = new UserUpdatedData();
    data.setUserId(userId);
    data.setName(name);
    data.setEmail(email);
    return data;
  }
}
