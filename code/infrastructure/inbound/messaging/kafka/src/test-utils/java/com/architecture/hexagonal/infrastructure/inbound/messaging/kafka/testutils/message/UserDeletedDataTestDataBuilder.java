package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedData;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserDeletedDataTestDataBuilder {

  @Builder.Default
  private final UUID userId = UUID.fromString("2b1f8f0e-6c3a-4f7e-9a53-0d6a6b0f1c11");

  public UserDeletedData userDeletedData() {
    final UserDeletedData data = new UserDeletedData();
    data.setUserId(userId);
    return data;
  }
}
