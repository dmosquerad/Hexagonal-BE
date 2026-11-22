package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.contract.bus.model.user.event.UserDeletedEvent;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.domain.port.out.bus.event.EventPublisher;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.RabbitMqClockConfig;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserDeletedEventMessageMapper;
import java.time.Clock;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeletedEventPublisher implements EventPublisher<UserDeletedEvent> {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserDeletedEventMessageMapper userDeletedEventMessageMapper;

  @Qualifier(RabbitMqClockConfig.RABBIT_MQ_CLOCK)
  private final Clock clock;

  @Override
  public void publish(final @NonNull UserDeletedEvent userDeletedEvent) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(DateVoFactory.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userDeletedEventMessageMapper.toUserDeleted(userDeletedEvent, messageHeaderVo));
  }
}
