package com.architecture.hexagonal.infrastructure.outbound.message.sender;

import com.architecture.hexagonal.infrastructure.outbound.message.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserUpdatedTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

@ExtendWith(MockitoExtension.class)
class UserMessageSenderTest {

  @InjectMocks UserMessageSender userMessageSender;

  @Mock StreamBridge streamBridge;

  @Test
  void sendUserCreatedMessage_shouldSendToStreamBridge_whenEventIsReceived() {
    final UserCreated event = UserCreatedTestDataBuilder.builder().build().userCreated();

    userMessageSender.sendUserCreatedMessage(event);

    Mockito.verify(streamBridge).send(UserMessageNaming.PUBLISH_USER_CREATED_OUT_BINDING, event);
  }

  @Test
  void sendUserUpdatedMessage_shouldSendToStreamBridge_whenEventIsReceived() {
    final UserUpdated event = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    userMessageSender.sendUserUpdatedMessage(event);

    Mockito.verify(streamBridge).send(UserMessageNaming.PUBLISH_USER_UPDATED_OUT_BINDING, event);
  }

  @Test
  void sendUserDeletedMessage_shouldSendToStreamBridge_whenEventIsReceived() {
    final UserDeleted event = UserDeletedTestDataBuilder.builder().build().userDeleted();

    userMessageSender.sendUserDeletedMessage(event);

    Mockito.verify(streamBridge).send(UserMessageNaming.PUBLISH_USER_DELETED_OUT_BINDING, event);
  }
}
