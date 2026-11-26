package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.domain.model.event.user.UserUpdatedEvent;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.domain.port.out.bus.event.EventPublisher;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.RabbitMqClockConfig;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserUpdatedEventMessageMapper;
import java.time.Clock;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdatedEventPublisher implements EventPublisher<UserUpdatedEvent> {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserUpdatedEventMessageMapper userUpdatedEventMessageMapper;

  @Qualifier(RabbitMqClockConfig.RABBIT_MQ_CLOCK)
  private final Clock clock;

  @Override
  public void publish(final @NonNull UserUpdatedEvent userUpdatedEvent) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(DateVoFactory.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userUpdatedEventMessageMapper.toUserUpdated(userUpdatedEvent, messageHeaderVo));
  }
}
