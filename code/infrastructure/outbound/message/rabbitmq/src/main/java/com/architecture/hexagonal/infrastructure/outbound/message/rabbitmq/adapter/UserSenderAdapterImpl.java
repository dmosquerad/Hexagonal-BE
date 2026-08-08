package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.adapter;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.UserMessageDaoMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSenderAdapterImpl implements UserSenderPort {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserMessageDaoMapper userMessageDaoMapper;

  @Override
  public void userSenderCreated(final @NonNull User user) {
    applicationEventPublisher.publishEvent(userMessageDaoMapper.toUserCreated(user));
  }

  @Override
  public void userSenderUpdated(final @NonNull User user) {
    applicationEventPublisher.publishEvent(userMessageDaoMapper.toUserUpdated(user));
  }

  @Override
  public void userSenderDeleted(final @NonNull User user) {
    applicationEventPublisher.publishEvent(userMessageDaoMapper.toUserDeleted(user));
  }
}
