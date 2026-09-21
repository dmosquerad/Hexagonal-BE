package com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.impl;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.ActionType;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming.OutboxNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOutboxServiceImpl implements OutboxService {

  private final ApplicationEventPublisher applicationEventPublisher;

  private final UserFromOutboxMapper userFromOutboxMapper;

  @Override
  public void process(final @NonNull Outbox outbox) {
    final Consumer<Outbox> handler = actionHandlers().get(outbox.action());
    if (Objects.isNull(handler)) {
      throw new IllegalArgumentException(OutboxNaming.UNSUPPORTED_USER_ACTION + outbox.action());
    }
    handler.accept(outbox);
  }

  private Map<String, Consumer<Outbox>> actionHandlers() {
    return Map.of(
        ActionType.UserActionType.USER_CREATED.getActionType(), this::senderUserCreated,
        ActionType.UserActionType.USER_UPDATED.getActionType(), this::senderUserUpdated,
        ActionType.UserActionType.USER_DELETED.getActionType(), this::senderUserDeleted);
  }

  private void senderUserCreated(final @NonNull Outbox outbox) {
    applicationEventPublisher.publishEvent(userFromOutboxMapper.toUserCreated(outbox.payload()));
  }

  private void senderUserUpdated(final @NonNull Outbox outbox) {
    applicationEventPublisher.publishEvent(userFromOutboxMapper.toUserUpdated(outbox.payload()));
  }

  private void senderUserDeleted(final @NonNull Outbox outbox) {
    applicationEventPublisher.publishEvent(userFromOutboxMapper.toUserDeleted(outbox.payload()));
  }
}
