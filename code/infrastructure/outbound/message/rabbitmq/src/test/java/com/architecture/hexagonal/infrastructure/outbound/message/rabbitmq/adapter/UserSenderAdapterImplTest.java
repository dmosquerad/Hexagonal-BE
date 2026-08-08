package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.adapter;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.UserMessageDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.message.UserUpdatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.model.entity.user.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.testutils.time.TestClock;
import java.time.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class UserSenderAdapterImplTest {

  @InjectMocks UserSenderAdapterImpl userSenderAdapterImpl;

  @Mock ApplicationEventPublisher applicationEventPublisher;

  @Spy UserMessageDaoMapper userMessageDaoMapper = Mappers.getMapper(UserMessageDaoMapper.class);

  @Spy private Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void userSenderCreated_shouldPublishUserCreatedEvent_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();

    userSenderAdapterImpl.userSenderCreated(user);

    Mockito.verify(userMessageDaoMapper)
        .toUserCreated(Mockito.eq(user), Mockito.any(MessageHeaderVo.class));
    Mockito.verify(clock).instant();
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserCreated.class));
  }

  @Test
  void userSenderUpdated_shouldPublishUserUpdatedEvent_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserUpdated userUpdated =
        UserUpdatedTestDataBuilder.builder().messageHeader(null).build().userUpdated();

    userSenderAdapterImpl.userSenderUpdated(user);

    Mockito.verify(userMessageDaoMapper)
        .toUserUpdated(Mockito.eq(user), Mockito.any(MessageHeaderVo.class));
    Mockito.verify(clock).instant();
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserUpdated.class));
  }

  @Test
  void userSenderDeleted_shouldPublishUserDeletedEvent_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();

    userSenderAdapterImpl.userSenderDeleted(user);

    Mockito.verify(userMessageDaoMapper)
        .toUserDeleted(Mockito.eq(user), Mockito.any(MessageHeaderVo.class));
    Mockito.verify(clock).instant();
    Mockito.verify(applicationEventPublisher).publishEvent(Mockito.any(UserDeleted.class));
  }
}
