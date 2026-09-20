package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.adapter;

import com.architecture.hexagonal.application.port.message.UserSenderPort;
import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user.UserMessageDaoMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSenderAdapterImpl implements UserSenderPort {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserMessageDaoMapper userMessageDaoMapper;
  private final Clock clock;

  @Override
  public void userSenderCreated(final @NonNull User user) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(OffsetDateTime.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userMessageDaoMapper.toUserCreated(user, messageHeaderVo));
  }

  @Override
  public void userSenderUpdated(final @NonNull User user) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(OffsetDateTime.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userMessageDaoMapper.toUserUpdated(user, messageHeaderVo));
  }

  @Override
  public void userSenderDeleted(final @NonNull User user) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(OffsetDateTime.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userMessageDaoMapper.toUserDeleted(user, messageHeaderVo));
  }
}
