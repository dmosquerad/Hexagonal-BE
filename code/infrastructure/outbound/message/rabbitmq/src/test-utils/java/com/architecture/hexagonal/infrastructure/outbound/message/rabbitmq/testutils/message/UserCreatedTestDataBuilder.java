package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.vo.outbox.MessageHeaderVoTestDataBuilder;
import lombok.Builder;

@Builder
public class UserCreatedTestDataBuilder {

  @Builder.Default
  private MessageHeaderVo messageHeader = MessageHeaderVoTestDataBuilder.builder().build().messageHeaderVo();

  @Builder.Default
  private User user = UserTestDataBuilder.builder().build().user();

  public UserCreated userCreated() {
    final UserCreated userCreated = new UserCreated();
      userCreated.setMessageHeader(messageHeader);
      userCreated.setData(user);

    return userCreated;
  }
}
