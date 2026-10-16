package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.event;

import com.architecture.hexagonal.application.port.in.bus.event.EventHandler;
import com.architecture.hexagonal.contract.bus.model.user.event.UserUpdatedEvent;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user.UserEventMessageMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdatedEventHandler implements EventHandler<UserUpdatedEvent> {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserEventMessageMapper userEventMessageMapper;
  private final Clock clock;

  @Override
  public void handle(final @NonNull UserUpdatedEvent userUpdatedEvent) {
    final MessageHeaderVo messageHeaderVo =
        MessageHeaderVo.builder()
            .messageDate(OffsetDateTime.now(clock))
            .messageId(UUID.randomUUID())
            .build();
    applicationEventPublisher.publishEvent(
        userEventMessageMapper.toUserUpdated(userUpdatedEvent, messageHeaderVo));
  }
}
