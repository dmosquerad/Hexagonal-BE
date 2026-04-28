package com.architecture.hexagonal.infrastructure.outbound.message.adapter;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.message.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.mapper.UserMessageDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.sender.UserMessageSender;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.entity.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserUpdatedTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(classes = {UserSenderAdapter.class})
@ContextConfiguration(classes = TestApplication.class)
class UserSenderAdapterTestIT {

  @Autowired
  UserSenderAdapter userSenderAdapter;

  @MockitoSpyBean
  UserMessageDaoMapper userMessageDaoMapper;

  @MockitoSpyBean
  UserMessageSender userMessageSender;

  @MockitoSpyBean
  StreamBridge streamBridge;

  @Autowired
  TransactionTemplate transactionTemplate;

  @Test
  void userSenderCreated_shouldPublishAndSendMessage_whenUserIsCreated() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserCreated userCreated = UserCreatedTestDataBuilder.builder().build().userCreated();

    transactionTemplate.execute(status -> {
      userSenderAdapter.userSenderCreated(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserCreated(user);
    Mockito.verify(streamBridge).send(UserMessageSender.PUBLISH_USER_CREATED_OUT_BINDING, userCreated);
  }

  @Test
  void userSenderUpdated_shouldPublishAndSendMessage_whenUserIsUpdated() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserUpdated userUpdated = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    transactionTemplate.execute(status -> {
      userSenderAdapter.userSenderUpdated(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserUpdated(user);
    Mockito.verify(streamBridge).send(UserMessageSender.PUBLISH_USER_UPDATED_OUT_BINDING, userUpdated);
  }

  @Test
  void userSenderDeleted_shouldPublishAndSendMessage_whenUserIsDeleted() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDeleted userDeleted = UserDeletedTestDataBuilder.builder().build().userDeleted();

    transactionTemplate.execute(status -> {
      userSenderAdapter.userSenderDeleted(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserDeleted(user);
    Mockito.verify(streamBridge).send(UserMessageSender.PUBLISH_USER_DELETED_OUT_BINDING, userDeleted);
  }
}
