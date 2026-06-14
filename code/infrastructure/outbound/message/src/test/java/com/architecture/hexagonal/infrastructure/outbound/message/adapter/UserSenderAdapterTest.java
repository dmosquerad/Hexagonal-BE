package com.architecture.hexagonal.infrastructure.outbound.message.adapter;

import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.data.UserUpdated;
import com.architecture.hexagonal.infrastructure.outbound.message.mapper.user.UserMessageDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.aggregate.UserTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserCreatedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserDeletedTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.message.testutils.data.message.UserUpdatedTestDataBuilder;
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
class UserSenderAdapterTest {

  @InjectMocks UserSenderAdapter userSenderAdapter;

  @Mock ApplicationEventPublisher applicationEventPublisher;

  @Spy UserMessageDaoMapper userMessageDaoMapper = Mappers.getMapper(UserMessageDaoMapper.class);

  @Test
  void userSenderCreated_shouldPublishUserCreatedEvent_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserCreated userCreated = UserCreatedTestDataBuilder.builder().build().userCreated();

    userSenderAdapter.userSenderCreated(user);

    Mockito.verify(userMessageDaoMapper).toUserCreated(user);
    Mockito.verify(applicationEventPublisher).publishEvent(userCreated);
  }

  @Test
  void userSenderUpdated_shouldPublishUserUpdatedEvent_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserUpdated userUpdated = UserUpdatedTestDataBuilder.builder().build().userUpdated();

    userSenderAdapter.userSenderUpdated(user);

    Mockito.verify(userMessageDaoMapper).toUserUpdated(user);
    Mockito.verify(applicationEventPublisher).publishEvent(userUpdated);
  }

  @Test
  void userSenderDeleted_shouldPublishUserDeletedEvent_whenUserIsValid() {
    final User user = UserTestDataBuilder.builder().build().user();
    final UserDeleted userDeleted = UserDeletedTestDataBuilder.builder().build().userDeleted();

    userSenderAdapter.userSenderDeleted(user);

    Mockito.verify(userMessageDaoMapper).toUserDeleted(user);
    Mockito.verify(applicationEventPublisher).publishEvent(userDeleted);
  }
}
