package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import lombok.Builder;

@Builder
public class UserCreatedDataTestDataBuilder {

  @Builder.Default private final String name = "John Doe";
  @Builder.Default private final String email = "john.doe@example.com";

  public UserCreatedData userCreatedData() {
    return new UserCreatedData(name, email);
  }
}
