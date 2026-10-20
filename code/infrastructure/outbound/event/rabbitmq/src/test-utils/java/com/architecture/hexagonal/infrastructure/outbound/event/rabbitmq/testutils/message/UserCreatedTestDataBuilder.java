package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.testutils.message;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.MessageHeader;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserData;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import lombok.Builder;

@Builder
public class UserCreatedTestDataBuilder {

  @Builder.Default
  private MessageHeader messageHeader = MessageHeaderTestDataBuilder.builder().build().messageHeader();

  @Builder.Default
  private UserData userData = UserDataTestDataBuilder.builder().build().userData();

  public UserCreated userCreated() {
    final UserCreated userCreated = new UserCreated();
    userCreated.setMessageHeader(messageHeader);
    userCreated.setData(userData);

    return userCreated;
  }
}
