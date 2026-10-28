package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.MessageHeader;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeletedPayload;
import lombok.Builder;

@Builder
public class UserDeletedPayloadTestDataBuilder {

  @Builder.Default
  private final MessageHeader messageHeader =
      MessageHeaderTestDataBuilder.builder().build().messageHeader();

  @Builder.Default
  private final UserDeletedData data = UserDeletedDataTestDataBuilder.builder().build().userDeletedData();

  public UserDeletedPayload userDeletedPayload() {
    final UserDeletedPayload payload = new UserDeletedPayload();
    payload.setMessageHeader(messageHeader);
    payload.setData_(data);
    return payload;
  }
}
