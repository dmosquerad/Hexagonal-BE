package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.MessageHeader;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreatedPayload;
import lombok.Builder;

@Builder
public class UserCreatedPayloadTestDataBuilder {

  @Builder.Default
  private final MessageHeader messageHeader =
      MessageHeaderTestDataBuilder.builder().build().messageHeader();

  @Builder.Default
  private final UserCreatedData data = UserCreatedDataTestDataBuilder.builder().build().userCreatedData();

  public UserCreatedPayload userCreatedPayload() {
    final UserCreatedPayload payload = new UserCreatedPayload();
    payload.setMessageHeader(messageHeader);
    payload.setData_(data);
    return payload;
  }
}
