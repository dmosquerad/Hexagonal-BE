package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.testutils.message;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.MessageHeader;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdatedPayload;
import lombok.Builder;

@Builder
public class UserUpdatedPayloadTestDataBuilder {

  @Builder.Default
  private final MessageHeader messageHeader =
      MessageHeaderTestDataBuilder.builder().build().messageHeader();

  @Builder.Default
  private final UserUpdatedData data = UserUpdatedDataTestDataBuilder.builder().build().userUpdatedData();

  public UserUpdatedPayload userUpdatedPayload() {
    final UserUpdatedPayload payload = new UserUpdatedPayload();
    payload.setMessageHeader(messageHeader);
    payload.setData_(data);
    return payload;
  }
}
