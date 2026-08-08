package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.impl;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming.OutboxNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.outbox.OutboxTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.aggregate.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.testutils.data.message.UserUpdatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserOutboxServiceImplTest {

  @InjectMocks private UserOutboxServiceImpl userOutboxServiceImpl;

  @Mock private UserSenderPort userSenderPort;

  @Spy
  private UserFromOutboxMapper userFromOutboxMapper = Mappers.getMapper(UserFromOutboxMapper.class);

  @Test
  void process_shouldSendCreatedMessage_whenActionIsUserCreated() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Outbox outboxDo =
        OutboxTestDataBuilder.builder()
            .action(UserMessageNaming.ACTION_USER_CREATED)
            .payload(UserCreatedTestDataBuilder.builder().build().userCreated())
            .build()
            .outbox();

    userOutboxServiceImpl.process(outboxDo);

    Mockito.verify(userFromOutboxMapper).toUser((UserCreated) outboxDo.payload());
    Mockito.verify(userSenderPort).userSenderCreated(user);
    Mockito.verifyNoMoreInteractions(userSenderPort);
  }

  @Test
  void process_shouldSendUpdatedMessage_whenActionIsUserUpdated() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Outbox outboxDo =
        OutboxTestDataBuilder.builder()
            .action(UserMessageNaming.ACTION_USER_UPDATED)
            .payload(UserUpdatedTestDataBuilder.builder().build().userUpdated())
            .build()
            .outbox();

    userOutboxServiceImpl.process(outboxDo);

    Mockito.verify(userFromOutboxMapper).toUser((UserUpdated) outboxDo.payload());
    Mockito.verify(userSenderPort).userSenderUpdated(user);
    Mockito.verifyNoMoreInteractions(userSenderPort);
  }

  @Test
  void process_shouldSendDeletedMessage_whenActionIsUserDeleted() {
    final User user = UserTestDataBuilder.builder().build().user();
    final Outbox outboxDo =
        OutboxTestDataBuilder.builder()
            .action(UserMessageNaming.ACTION_USER_DELETED)
            .payload(UserDeletedTestDataBuilder.builder().build().userDeleted())
            .build()
            .outbox();

    userOutboxServiceImpl.process(outboxDo);

    Mockito.verify(userFromOutboxMapper).toUser((UserDeleted) outboxDo.payload());
    Mockito.verify(userSenderPort).userSenderDeleted(user);
    Mockito.verifyNoMoreInteractions(userSenderPort);
  }

  @Test
  void process_shouldThrowIllegalArgumentException_whenActionIsUnsupported() {
    final Outbox outboxDo =
        OutboxTestDataBuilder.builder().action("").payload("{}").build().outbox();

    AssertionsForClassTypes.assertThatThrownBy(() -> userOutboxServiceImpl.process(outboxDo))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(OutboxNaming.UNSUPPORTED_USER_ACTION);

    Mockito.verifyNoInteractions(userSenderPort);
  }
}
