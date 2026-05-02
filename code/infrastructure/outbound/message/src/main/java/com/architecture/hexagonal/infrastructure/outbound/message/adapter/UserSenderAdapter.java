package com.architecture.hexagonal.infrastructure.outbound.message.adapter;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.infrastructure.outbound.message.mapper.user.UserMessageDaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSenderAdapter implements UserSenderPort {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserMessageDaoMapper userMessageDaoMapper;

  @Override
  public void userSenderCreated(final User user) {
    applicationEventPublisher.publishEvent(userMessageDaoMapper.toUserCreated(user));
  }

  @Override
  public void userSenderUpdated(final User user) {
    applicationEventPublisher.publishEvent(userMessageDaoMapper.toUserUpdated(user));
  }

  @Override
  public void userSenderDeleted(final User user) {
    applicationEventPublisher.publishEvent(userMessageDaoMapper.toUserDeleted(user));
  }
}
