package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.contract.bus.model.user.event.UserCreatedEvent;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.time.factory.DateVoFactory;
import com.architecture.hexagonal.domain.port.out.bus.event.EventPublisher;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.RabbitMqClockConfig;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserCreatedEventMessageMapper;
import java.time.Clock;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreatedEventPublisher implements EventPublisher<UserCreatedEvent> {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserCreatedEventMessageMapper userCreatedEventMessageMapper;

  @Qualifier(RabbitMqClockConfig.RABBIT_MQ_CLOCK)
  private final Clock clock;

  @Override
  public void publish(final @NonNull UserCreatedEvent userCreatedEvent) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(DateVoFactory.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userCreatedEventMessageMapper.toUserCreated(userCreatedEvent, messageHeaderVo));
  }
}
