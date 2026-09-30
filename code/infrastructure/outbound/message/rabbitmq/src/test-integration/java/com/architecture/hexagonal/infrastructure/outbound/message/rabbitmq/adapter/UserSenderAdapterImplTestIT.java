package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.UserMessageDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserPublishBinding;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender.UserMessageSender;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.entity.user.UserTestDataBuilder;
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

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderCreated(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserCreated(Mockito.eq(user), Mockito.any(MessageHeaderVo.class));
    Mockito.verify(streamBridge).send(Mockito.eq(UserPublishBinding.USER_CREATED.getPublishBinding()), Mockito.any(UserCreated.class));
  }

  @Test
  void userSenderUpdated_shouldPublishAndSendMessage_whenUserIsUpdated() {
    final User user = UserTestDataBuilder.builder().build().user();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderUpdated(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserUpdated(Mockito.eq(user), Mockito.any(MessageHeaderVo.class));
    Mockito.verify(streamBridge).send(Mockito.eq(UserPublishBinding.USER_UPDATED.getPublishBinding()), Mockito.any(UserUpdated.class));
  }

  @Test
  void userSenderDeleted_shouldPublishAndSendMessage_whenUserIsDeleted() {
    final User user = UserTestDataBuilder.builder().build().user();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderDeleted(user);
      return null;
    });

    Mockito.verify(userMessageDaoMapper).toUserDeleted(Mockito.eq(user), Mockito.any(MessageHeaderVo.class));
    Mockito.verify(streamBridge).send(Mockito.eq(UserPublishBinding.USER_DELETED.getPublishBinding()), Mockito.any(UserDeleted.class));
  }
}
