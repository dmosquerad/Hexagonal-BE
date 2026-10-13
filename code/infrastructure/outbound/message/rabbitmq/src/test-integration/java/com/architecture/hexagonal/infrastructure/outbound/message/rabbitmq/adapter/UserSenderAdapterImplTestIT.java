package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.adapter;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryWritePort;
import com.architecture.hexagonal.contract.outbound.rabbitmq.MessageHeader;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserData;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.domain.testutils.data.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.TestApplication;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.IUserEventsProducer;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.UserMessageDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.sender.UserMessageSender;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserDataTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.MessageHeaderTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.support.TransactionTemplate;

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
  IUserEventsProducer userEventStreamPublisher;

  @Autowired
  TransactionTemplate transactionTemplate;

  @Test
  void userSenderCreatedShouldPublishAndSendMessageWhenUserIsCreated() {
    final User user = UserTestDataBuilder.builder().build().user();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderCreated(user);
      return null;
    });

    final ArgumentCaptor<MessageHeaderVo> headerCaptor =
        ArgumentCaptor.forClass(MessageHeaderVo.class);
    Mockito.verify(userMessageDaoMapper).toUserCreated(Mockito.eq(user), headerCaptor.capture());
    Assertions.assertThat(headerCaptor.getValue().messageId()).isNotNull();
    Assertions.assertThat(headerCaptor.getValue().messageDate()).isNotNull();

    final ArgumentCaptor<UserCreated> eventCaptor = ArgumentCaptor.forClass(UserCreated.class);
    Mockito.verify(userEventStreamPublisher).publishUserCreated(eventCaptor.capture());
    final UserData expectedUserData =
        UserDataTestDataBuilder.builder()
            .userId(user.userId())
            .name(user.name())
            .email(user.email())
            .build()
            .userData();
    final MessageHeaderVo messageHeader = headerCaptor.getValue();
    final MessageHeader expectedMessageHeader =
        MessageHeaderTestDataBuilder.builder()
            .messageId(messageHeader.messageId())
            .messageDate(messageHeader.messageDate())
            .build()
            .messageHeader();
    final UserCreated expectedEvent =
        UserCreatedTestDataBuilder.builder()
            .userData(expectedUserData)
            .messageHeader(expectedMessageHeader)
            .build()
            .userCreated();
    Assertions.assertThat(eventCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expectedEvent);
  }

  @Test
  void userSenderUpdatedShouldPublishAndSendMessageWhenUserIsUpdated() {
    final User user = UserTestDataBuilder.builder().build().user();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderUpdated(user);
      return null;
    });

    final ArgumentCaptor<MessageHeaderVo> headerCaptor =
        ArgumentCaptor.forClass(MessageHeaderVo.class);
    Mockito.verify(userMessageDaoMapper).toUserUpdated(Mockito.eq(user), headerCaptor.capture());
    Assertions.assertThat(headerCaptor.getValue().messageId()).isNotNull();
    Assertions.assertThat(headerCaptor.getValue().messageDate()).isNotNull();

    final ArgumentCaptor<UserUpdated> eventCaptor = ArgumentCaptor.forClass(UserUpdated.class);
    Mockito.verify(userEventStreamPublisher).publishUserUpdated(eventCaptor.capture());
    final UserData expectedUserData =
        UserDataTestDataBuilder.builder()
            .userId(user.userId())
            .name(user.name())
            .email(user.email())
            .build()
            .userData();
    final MessageHeaderVo messageHeader = headerCaptor.getValue();
    final MessageHeader expectedMessageHeader =
        MessageHeaderTestDataBuilder.builder()
            .messageId(messageHeader.messageId())
            .messageDate(messageHeader.messageDate())
            .build()
            .messageHeader();
    final UserUpdated expectedEvent =
        UserUpdatedTestDataBuilder.builder()
            .userData(expectedUserData)
            .messageHeader(expectedMessageHeader)
            .build()
            .userUpdated();
    Assertions.assertThat(eventCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expectedEvent);
  }

  @Test
  void userSenderDeletedShouldPublishAndSendMessageWhenUserIsDeleted() {
    final User user = UserTestDataBuilder.builder().build().user();

    transactionTemplate.execute(status -> {
      userSenderAdapterImpl.userSenderDeleted(user);
      return null;
    });

    final ArgumentCaptor<MessageHeaderVo> headerCaptor =
        ArgumentCaptor.forClass(MessageHeaderVo.class);
    Mockito.verify(userMessageDaoMapper).toUserDeleted(Mockito.eq(user), headerCaptor.capture());
    Assertions.assertThat(headerCaptor.getValue().messageId()).isNotNull();
    Assertions.assertThat(headerCaptor.getValue().messageDate()).isNotNull();

    final ArgumentCaptor<UserDeleted> eventCaptor = ArgumentCaptor.forClass(UserDeleted.class);
    Mockito.verify(userEventStreamPublisher).publishUserDeleted(eventCaptor.capture());
    final UserData expectedUserData =
        UserDataTestDataBuilder.builder()
            .userId(user.userId())
            .name(user.name())
            .email(user.email())
            .build()
            .userData();
    final MessageHeaderVo messageHeader = headerCaptor.getValue();
    final MessageHeader expectedMessageHeader =
        MessageHeaderTestDataBuilder.builder()
            .messageId(messageHeader.messageId())
            .messageDate(messageHeader.messageDate())
            .build()
            .messageHeader();
    final UserDeleted expectedEvent =
        UserDeletedTestDataBuilder.builder()
            .userData(expectedUserData)
            .messageHeader(expectedMessageHeader)
            .build()
            .userDeleted();
    Assertions.assertThat(eventCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(expectedEvent);
  }
}
