package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.UserMessageDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender.UserMessageSender;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.data.message.UserUpdatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.time.TestClock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;

@SpringBootTest(classes = {UserSenderAdapterImpl.class})
@ContextConfiguration(classes = TestApplication.class)
class UserSenderAdapterImplTestIT {

  @Autowired
  UserSenderAdapterImpl userSenderAdapterImpl;

  @MockitoSpyBean
  UserMessageDaoMapper userMessageDaoMapper;

  @MockitoSpyBean
  UserMessageSender userMessageSender;

  @MockitoBean
  OutboxRepositoryWritePort outboxRepositoryWritePort;

  @MockitoSpyBean
  StreamBridge streamBridge;

  @MockitoSpyBean
  Clock clock = TestClock.FIXED_CLOCK;

  @Autowired
  TransactionTemplate transactionTemplate;

  @Test
  void userSenderCreated_shouldPublishAndSendMessage_whenUserIsCreated() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserCreated userCreated = UserCreatedTestDataBuilder.builder().build().userCreated();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderCreated(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserCreated(user);
    Mockito.verify(streamBridge).send(UserMessageNaming.PUBLISH_USER_CREATED_OUT_BINDING, userCreated);
  }

  @Test
  void userSenderUpdated_shouldPublishAndSendMessage_whenUserIsUpdated() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserUpdated userUpdated = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderUpdated(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserUpdated(user);
    Mockito.verify(streamBridge).send(UserMessageNaming.PUBLISH_USER_UPDATED_OUT_BINDING, userUpdated);
  }

  @Test
  void userSenderDeleted_shouldPublishAndSendMessage_whenUserIsDeleted() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDeleted userDeleted = UserDeletedTestDataBuilder.builder().build().userDeleted();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderDeleted(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserDeleted(user);
    Mockito.verify(streamBridge).send(UserMessageNaming.PUBLISH_USER_DELETED_OUT_BINDING, userDeleted);
  }
}
