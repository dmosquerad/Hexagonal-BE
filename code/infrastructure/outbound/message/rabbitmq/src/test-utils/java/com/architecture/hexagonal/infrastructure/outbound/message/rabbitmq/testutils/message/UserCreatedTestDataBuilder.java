package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message;

import com.architecture.hexagonal.contract.outbound.rabbitmq.MessageHeader;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserData;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.entity.user.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.vo.outbox.MessageHeaderTestDataBuilder;
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
